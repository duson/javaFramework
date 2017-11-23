package com.facewnd.core.web;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;

public class HttpServletResponseWrapperEx extends HttpServletResponseWrapper {
	private ServletOutputStream outputStream;
	private PrintWriter writer;
	private ServletOutputStreamWrapper cout;

	public HttpServletResponseWrapperEx(HttpServletResponse response) {
		super(response);
	}

	@Override
	public ServletOutputStream getOutputStream() throws IOException {
		if (writer != null) {
			throw new IllegalStateException("getWriter() has already been called on this response.");
		}

		if (outputStream == null) {
			outputStream = getResponse().getOutputStream();
			cout = new ServletOutputStreamWrapper(outputStream);
		}
		return cout;
	}

	@Override
	public PrintWriter getWriter() throws IOException {
		if (outputStream != null) {
			throw new IllegalStateException("getOutputStream() has already been called on this response.");
		}

		if (writer == null) {
			cout = new ServletOutputStreamWrapper(getResponse().getOutputStream());
			writer = new PrintWriter(new OutputStreamWriter(cout, getResponse().getCharacterEncoding()), true);
		}
		return writer;
	}

	@Override
	public void flushBuffer() throws IOException {
		if (writer != null) {
			writer.flush();
		} else if (outputStream != null) {
			cout.flush();
		}
	}

	public byte[] getCopy() {
		if (cout != null) {
			return cout.getCopy();
		} else {
			return new byte[0];
		}
	}
}
