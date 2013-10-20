package util

import org.slf4j.LoggerFactory

trait Logging {
	lazy val logger = LoggerFactory.getLogger(this.getClass())

	def debug = (message: String) =>
		logger.debug(message)

	def warn = (message: String) =>
		logger.warn(message)

	def error = (message: String) =>
		logger.error(message)

}