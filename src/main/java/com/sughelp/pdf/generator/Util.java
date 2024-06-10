package com.sughelp.pdf.generator;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Anish
 *
 * @since 09-May-2020
 */
public class Util {

	private static final Logger logger = LoggerFactory.getLogger(Util.class);

	public static byte[] downloadFile(String url) {
		logger.info("downloading file from server");
		InputStream is = null;
		try {
			URLConnection conn = new URL(url).openConnection();
			conn.setConnectTimeout(10000);
			conn.setReadTimeout(10000);
			conn.connect();
			is = conn.getInputStream();
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			byte[] byteChunk = new byte[4096];
			int n;
			while ((n = is.read(byteChunk)) > 0) {
				baos.write(byteChunk, 0, n);
			}
			logger.info("file downloaded successfully");
			return baos.toByteArray();
		} catch (IOException e) {
			logger.error("error while downloading file from server", e);
			return null;
		} finally {
			if (is != null) {
				try {
					is.close();
				} catch (IOException e) {
					logger.error("error while closing url connection. Please check if there is any leak", e);
				}

			}
		}
	}

}
