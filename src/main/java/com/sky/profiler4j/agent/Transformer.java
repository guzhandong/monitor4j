package com.sky.profiler4j.agent;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.security.ProtectionDomain;
import java.util.ArrayList;
import java.util.List;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;

import com.sky.profiler4j.agent.util.ClassesChoose;
import com.sky.profiler4j.aspect.asm.CustomClassVisitor;

public class Transformer implements ClassFileTransformer {

	final static List<String> transform_after_Classese = new ArrayList<String>();

	public byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined,
			ProtectionDomain protectionDomain, byte[] classfileBuffer) throws IllegalClassFormatException {

		boolean c = false;
		c = ClassesChoose.isWantTransformer(classfileBuffer);

		if (c) {

			try {
				ClassReader classReader = null;
				classReader = new ClassReader(classfileBuffer);

				// 开始转换类的字节码
				ClassWriter classWriter = new ClassWriter(ClassWriter.COMPUTE_MAXS);
				CustomClassVisitor customClassVisitor = new CustomClassVisitor(Opcodes.ASM5, classWriter, className);
				classReader.accept(customClassVisitor, 0);
				byte[] classfileBuffer_ = classWriter.toByteArray();

				// 记录已经转换过的类，避免重复转换
				Transformer.addTransform_after_Classese(className);
				if (className.equals("test/service/PVUVServiceImpl")) {

					File file = new File("h:/test/asm/PVUVServiceImpl.class");
					FileOutputStream fout;
					try {
						fout = new FileOutputStream(file);
						fout.write(classfileBuffer_);
						fout.close();
					} catch (FileNotFoundException e) {
						e.printStackTrace();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
				return classfileBuffer_;
			} catch (Exception e) {
				e.printStackTrace();
				return classfileBuffer;
			}
		} else {
			return classfileBuffer;
		}
	}

	public static boolean isTransform(String className) {
		for (String clazz1 : transform_after_Classese) {
			if (clazz1.equals(className)) {
				return true;
			}
		}
		return false;
	}

	public static void addTransform_after_Classese(String className) {
		transform_after_Classese.add(className);
	}
}