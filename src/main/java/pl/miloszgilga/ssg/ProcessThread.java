package pl.miloszgilga.ssg;

import org.apache.commons.vfs2.FileObject;
import org.apache.commons.vfs2.FileSystemManager;
import org.apache.commons.vfs2.VFS;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

abstract class ProcessThread extends Thread {
	protected static final double DEF_FACTOR = 5.0d;
	private static final Logger LOG = LoggerFactory.getLogger(ProcessThread.class);

	protected final ExecutorService executor;
	protected final List<Callable<Void>> tasks;

	ProcessThread(String name) {
		super(name);
		executor = Executors.newFixedThreadPool(threadPoolSize());
		tasks = new ArrayList<>();
	}

	@Override
	public void run() {
		try {
			runWithExceptionPreserve();
			final List<Future<Void>> results = executor.invokeAll(tasks);
			for (final Future<Void> result : results) {
				result.get();
			}
		} catch (InoperableException ex) {
			ex.logError();
		} catch (Exception ex) {
			LOG.error("Some error occured in thread pool processing chain. Cause: {}", ex.getMessage());
			System.exit(-1);
		} finally {
			executor.shutdown();
			for (final AutoCloseable closeable : closeables()) {
				try {
					closeable.close();
				} catch (Exception ignored) {
				}
			}
		}
	}

	public void startAndJoin() throws InterruptedException {
		start();
		join();
	}

	public void addTask(CallableThread callableThread) {
		tasks.add(() -> {
			callableThread.run();
			return null;
		});
	}

	protected int getCountOfProcessingContent(String directoryPath) {
		int count = -1;
		try {
			String directoryPathWithoutTrailingSlash = directoryPath;
			if (directoryPath.startsWith("/")) {
				directoryPathWithoutTrailingSlash = directoryPath.substring(1);
			}
			final URL resourceUrl = getClass().getClassLoader().getResource(directoryPathWithoutTrailingSlash);
			if (resourceUrl == null) {
				throw new IOException("Directory: " + directoryPath + " not exist.");
			}
			final FileSystemManager fsManager = VFS.getManager();
			final FileObject directory = fsManager.resolveFile(resourceUrl);

			final Deque<FileObject> stack = new ArrayDeque<>();
			stack.push(directory);
			while (!stack.isEmpty()) {
				final FileObject currentDir = stack.pop();
				if (!currentDir.exists() || currentDir.isFile()) {
					continue;
				}
				final FileObject[] children = currentDir.getChildren();
				for (FileObject child : children) {
					if (child.isFolder()) {
						stack.push(child);
					} else {
						count++;
					}
				}
				currentDir.close();
			}
		} catch (IOException ex) {
			throw new InoperableException(getClass(), "Unable to get count of files in path: %s. Cause: %s.",
				directoryPath, ex.getMessage());
		}
		return count;
	}

	protected abstract void runWithExceptionPreserve() throws Exception;

	protected abstract int threadPoolSize();

	protected abstract List<AutoCloseable> closeables();
}
