package com.sky.profiler4j.agent.aspect.asm;

import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.commons.AdviceAdapter;

import com.sky.profiler4j.agent.profile.threadStack.ThreadProfiler;

public class CustomMethodAdviceAdapter extends AdviceAdapter {

	private int method_id;

	private static final String threadProfilerClassName = ThreadProfiler.class.getName().replaceAll("\\.", "/");

	public CustomMethodAdviceAdapter(int api, MethodVisitor mv, int access, String name, String desc, int method_id) {
		super(api, mv, access, name, desc);
		this.method_id = method_id;
	}

	@Override
	public void visitLineNumber(int line, Label start) {
		super.visitLineNumber(line, start);
	}

	@Override
	public void visitLocalVariable(String name, String desc, String signature, Label start, Label end, int index) {
		super.visitLocalVariable(name, desc, signature, start, end, index);
	}

	@Override
	public void visitLabel(Label label) {
		super.visitLabel(label);
	}

	@Override
	protected void onMethodEnter() {
		// super.visitCode();

		// threadMethod
		mv.visitLdcInsn(new Integer(method_id));
		mv.visitMethodInsn(INVOKESTATIC, threadProfilerClassName, "enterMethod", "(I)V", false);

		// http

		// sql

		// webservice

		//
	}

	@Override
	protected void onMethodExit(int opcode) {

		mv.visitLdcInsn(new Integer(method_id));
		mv.visitMethodInsn(INVOKESTATIC, threadProfilerClassName, "exitMethod", "(I)V", false);

		// http

		// sql

		// webservice

		//
	}

	@Override
	public void visitMaxs(int maxStack, int maxLocals) {
		if (maxStack > 1) {
			super.visitMaxs(maxStack, maxLocals + 3);
		} else {
			super.visitMaxs(maxStack + 1, maxLocals + 3);
		}
	}

}
