package com.facewnd.core.web;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import javax.servlet.ServletOutputStream;
import javax.servlet.WriteListener;

public class ServletOutputStreamWrapper extends ServletOutputStream {
	private OutputStream outputStream;
	private ByteArrayOutputStream byteArrayOutputStream;

	public ServletOutputStreamWrapper(OutputStream outputStream) {
		this.outputStream = outputStream;
		byteArrayOutputStream = new ByteArrayOutputStream();
	}

	@Override
	public boolean isReady() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void setWriteListener(WriteListener arg0) {
	}

	@Override
	public void write(int b) throws IOException {
		outputStream.write(b);
        byteArrayOutputStream.write(b);
	}
	
	public byte[] getCopy(){
        return byteArrayOutputStream.toByteArray();
    }

}
