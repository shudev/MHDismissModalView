package com.shuketa.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;


public class PreventMultiProcess {
	 /**
		 * ロックファイルの名前.<br>
		 */
		public static final String LOCK_FILE_NAME = ".lock";
	 
		/**
		 * ロックファイルの出力ストリーム.<br>
		 */
		private FileOutputStream fos;
	 
		/**
		 * このアプリケーションが同じデータ保存先を操作しないように
		 * 排他ロックするためのロックオブジェクト.<br>
		 */
		private FileLock lock;
	 
		/**
		 * コンストラクタ.<br>
		 * 構築時にはロックファイルは作成しますがロックはかけられていません。
		 * @param baseDir ロックファイルを置くディレクトリ
		 * @throws IOException 失敗
		 */
		public PreventMultiProcess(File baseDir) throws IOException {
			if (baseDir == null) {
				throw new IllegalArgumentException();
			}
	 
			// ディレクトリが、まだ存在しなければ作成する.
			if (!baseDir.exists()) {
				baseDir.mkdirs();
			}
	 
			// ロックファイル
			File lockFile = new File(baseDir, LOCK_FILE_NAME);
	 
			// ロックファイルをオープンする.
			this.fos = new FileOutputStream(lockFile);
		}


		/**
		 * 同じデータ保存先を示して複数起動していないかチェックする.<br>
		 * ロックファイルは引数で指定されたディレクトリ上に作成される.<br>
		 *
		 * @param baseDir ロックファイルを配置するディレクトリ
		 * @return 問題なければtrue、排他されている場合はfalse
		 */
		public boolean tryLock() throws IOException {
			
			if (this.lock != null) {
				return false;
			}
			try {
				// ロックの取得を試行する.
				FileChannel channel = fos.getChannel();
				FileLock lock = channel.tryLock();
				if (lock != null) {
					// ロックが取得された場合
					this.lock = lock;
					return true;
				}
	 
			} catch (IOException ex) {
				// ロック取得時に例外が発生した場合
				throw new IOException("Failed to control a lock file.", ex);
			}

			// ロックが取れなかった場合
			return false;
		}
	 
		/**
		 * ロックされている状態であるか?
		 * @return ロックされている状態であればtrue
		 */
		public boolean isLocked() {
			return lock != null;
		}
	 
		/**
		 * ロックをリリースする.
		 * @throws IOException
		 */
		public void release() throws IOException {
			FileLock lock = this.lock;
			if (lock != null) {
				lock.release();
				this.lock = null;
			}
		}
	 
		/**
		 * ロックされていればロックをリリースし、
		 * ロックファイルをクローズする.
		 */
		public void close() throws IOException {
			try {
				release();
	 
			} catch (IOException ex) {
				// 無視する.
			}
	 
			if (fos != null) {
				fos.close();
			}
		}
	 
}
