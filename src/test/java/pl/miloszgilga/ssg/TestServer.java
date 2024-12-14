/*
 * Copyright (c) 2024 by MILOSZ GILGA <https://miloszgilga.pl>
 * You can find the licenses for this software in the LICENSE file.
 */
package pl.miloszgilga.ssg;

import io.javalin.Javalin;
import io.javalin.http.staticfiles.Location;

public class TestServer {
	public static void main(String[] args) {
		final int port = Integer.parseInt(System.getProperty("server.port"));
		Javalin
			.create(config -> config.staticFiles.add("output", Location.EXTERNAL))
			.start(port);
	}
}
