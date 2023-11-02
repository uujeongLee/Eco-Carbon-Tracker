/*package common.file;

import java.io.IOException;

import net.bramp.ffmpeg.FFmpeg;
import net.bramp.ffmpeg.FFmpegExecutor;
import net.bramp.ffmpeg.FFprobe;
import net.bramp.ffmpeg.builder.FFmpegBuilder;

public class Ffmpeg {

	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		
		
		FFmpeg ffmpeg = new FFmpeg("/plugins/ffmpeg/ffmpeg.exe");
		FFprobe ffprobe = new FFprobe("/plugins/ffmpeg/ffprobe.exe");
		
		FFmpegBuilder builder = new FFmpegBuilder().setInput("D:/UPLOAD_FILE/video/car_uplaod_sample.mp4")
					.overrideOutputFiles(true)
					.addOutput("D:/UPLOAD_FILE/video/ffmpeg_sample.mp4") 
					.setFormat("mp4") 
					.setVideoCodec("libx264")
					.disableSubtitle() 
					.setAudioChannels(2) 
					.setVideoResolution(1280, 720)
					.setVideoBitRate(1464800)
					.setStrict(FFmpegBuilder.Strict.EXPERIMENTAL)
					.done();
			
			FFmpegExecutor executor = new FFmpegExecutor(ffmpeg, ffprobe);
			executor.createJob(builder).run();
	}

}
*/