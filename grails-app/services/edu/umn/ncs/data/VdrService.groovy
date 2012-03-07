package edu.umn.ncs.data

import java.security.MessageDigest

class VdrService {

	def saveStreamToFile(inputStream, fileName) {
		def result = [ bytesRead:0, md5Sum:'' ]

			// get a file to write to
			def fileOutputStream = new File(fileName).newDataOutputStream()

			// create a 1k buffer for data transfer
			byte[] transferBuffer = new byte[1024]
			// this will store the bytes read as we go along
			int bytesRead
			long totalBytesRead = 0

			// We'll use this to calculate the MD5 sum
			MessageDigest md5Digester = MessageDigest.getInstance("MD5")

			while ( (bytesRead = inputStream.read(transferBuffer)) > 0 ) {
				// update MD5
				md5Digester.update(transferBuffer, 0, bytesRead)
				fileOutputStream.write(transferBuffer,0, bytesRead)
				totalBytesRead += bytesRead
			}
			// flush the stream
			fileOutputStream.close()
			inputStream.close()
			def md5Digest = md5Digester.digest()
			def md5Sum = byteArrayToString(md5Digest)

			result.bytesRead = totalBytesRead
			result.md5Sum = md5Sum

		return result
	}

	String byteArrayToString(byte[] byteArray) {
		def asHex = new StringBuffer()
		byteArray.eachByte{
			asHex << Integer.toString( ( it & 0xff ) + 0x100, 16).substring( 1 );
		}
		return asHex
	}

}
