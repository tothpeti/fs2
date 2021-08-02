/*
 * Copyright (c) 2013 Functional Streams for Scala
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of
 * this software and associated documentation files (the "Software"), to deal in
 * the Software without restriction, including without limitation the rights to
 * use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of
 * the Software, and to permit persons to whom the Software is furnished to do so,
 * subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS
 * FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR
 * COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER
 * IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN
 * CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package fs2
package io
package file

import scala.scalajs.js
import scala.util.control.NoStackTrace

class FileSystemException(message: String = null, cause: Throwable = null)
    extends IOException(message, cause)
object FileSystemException {
  private[io] def unapply(cause: js.JavaScriptException): Option[FileSystemException] =
    FileAlreadyExistsException.unapply(cause).orElse(NoSuchFileException.unapply(cause))
}

class FileAlreadyExistsException(message: String = null, cause: Throwable = null)
    extends FileSystemException(message, cause)
private class JavaScriptFileAlreadyExistsException(cause: js.JavaScriptException)
    extends FileAlreadyExistsException(cause = cause)
    with NoStackTrace
object FileAlreadyExistsException {
  private[file] def unapply(cause: js.JavaScriptException): Option[FileAlreadyExistsException] =
    cause match {
      case js.JavaScriptException(error: js.Error) if error.message.contains("EEXIST") =>
        Some(new JavaScriptFileAlreadyExistsException(cause))
      case _ => None
    }
}

class NoSuchFileException(message: String = null, cause: Throwable = null)
    extends FileSystemException(message, cause)
private class JavaScriptNoSuchFileException(cause: js.JavaScriptException)
    extends NoSuchFileException(cause = cause)
    with NoStackTrace
object NoSuchFileException {
  private[file] def unapply(cause: js.JavaScriptException): Option[NoSuchFileException] =
    cause match {
      case js.JavaScriptException(error: js.Error) if error.message.contains("ENOENT") =>
        Some(new JavaScriptNoSuchFileException(cause))
      case _ => None
    }
}
