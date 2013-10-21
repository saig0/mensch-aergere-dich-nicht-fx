package util

import org.slf4j.LoggerFactory

trait Logging {
	private lazy val logger = LoggerFactory.getLogger(this.getClass())

	def debug(message: String) =
		logger.debug(message)

	def warn(message: String) =
		logger.warn(message)

	def warn(message: String, cause: Throwable) =
		logger.warn(message, cause)

	def error(message: String) =
		logger.error(message)

	def error(message: String, cause: Throwable) =
		logger.error(message, cause)

}