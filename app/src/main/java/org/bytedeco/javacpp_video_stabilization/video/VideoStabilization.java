/*
 * Copyright (C) 2018 Monxarat
 *
 * Licensed either under the Apache License, Version 2.0, or (at your option)
 * under the terms of the GNU General Public License as published by
 * the Free Software Foundation (subject to the "Classpath" exception),
 * either version 2, or any later version (collectively, the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *     http://www.gnu.org/licenses/
 *     http://www.gnu.org/software/classpath/license.html
 *
 * or as provided in the LICENSE.txt file that accompanied this code.
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL
 * THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package org.bytedeco.javacpp_video_stabilization.video;


import android.util.Log;

import org.bytedeco.javacpp.opencv_core;
import org.bytedeco.javacpp.opencv_core.Mat;
import org.bytedeco.javacpp.opencv_features2d.GFTTDetector;
import org.bytedeco.javacpp.opencv_imgcodecs;
import org.bytedeco.javacpp.opencv_videoio.VideoWriter;
import org.bytedeco.javacpp.opencv_videostab.IFrameSource;
import org.bytedeco.javacpp.opencv_videostab.IOutlierRejector;
import org.bytedeco.javacpp.opencv_videostab.KeypointBasedMotionEstimator;
import org.bytedeco.javacpp.opencv_videostab.MotionEstimatorRansacL2;
import org.bytedeco.javacpp.opencv_videostab.NullOutlierRejector;
import org.bytedeco.javacpp.opencv_videostab.RansacParams;
import org.bytedeco.javacpp.opencv_videostab.TwoPassStabilizer;
import org.bytedeco.javacv.FFmpegFrameGrabber;
import org.bytedeco.javacv.FrameGrabber;
import org.bytedeco.javacv.OpenCVFrameConverter;

import static org.bytedeco.javacpp.opencv_core.flip;
import static org.bytedeco.javacpp.opencv_core.transpose;

/**
 * Created by chinhnq on 7/12/18.
 *
 * <p>Video stabilization refers to a family of methods used to reduce the blurring associated with the
 * motion of the camera. In other words, it compensates for any angular movement, equivalent to yaw,
 * pitch, roll, and x and y translations of the camera. The first image stabilizers appeared in the early 60s.
 * These systems were able to slightly compensate for camera shakes and involuntary movements.
 * They were controlled by gyroscopes and accelerometers based on mechanisms that could cancel or
 * reduce unwanted movement by changing the position of a lens. Currently, these methods are widely
 * used in binoculars, video cameras, and telescopes.</p>
 *
 * <p>Video-stabilization algorithms usually encompass the following steps:</p>
 *
 * <p>
 *     <b>Source Video Frames</b></>
 *     -> Motion Estimator
 *     -> <b>Matrices of Motion</b>
 *     -> Transformation of Frames
 *     -> <b>Stabilized Video Frames with some Detects</b>
 *     -> Video Completion, Debluring,etc.
 *     -> <b>Stabilized Video</b>.</p>
 */
public class VideoStabilization {

    /** stored frame of the video */
    private IFrameSource mFrameSource;

    /** source video */
    private String sourceVideo;

    /** destination of the video*/
    private String destVideo;

    /** extract callback */
    private FFmpegFrameGrabber frameGrabber;

    /** size video or image you want to export */
    private opencv_core.Size mOutImgSize;

    /** Rotate video if you want, by default will be rotate 90 degree*/
    private int rotateVideo;

    private VizoVideoStabConfig mVizoVideoStabConfig;

    private OnStabilizedListener onStabilizedListener;

    public VideoStabilization(String sourcePath, String destPath) {
        this.sourceVideo = sourcePath;
        this.destVideo = destPath;
        mVizoVideoStabConfig = new VizoVideoStabConfig();
    }

    /**
     * processing video will be over 4th step below.
     * <ur>
     *     <li>1 - prepare the input video and check it string input</li>
     *     <li>2 - prepare the motion estimator</li>
     *     <li>3 - Prepare stabilizer</li>
     *     <li>4 - Processing the stabilized frames. The results are showed and saved.</li>
     * </ur>
     *
     */
    public void stabilizer() {
        try{
            if (onStabilizedListener != null) {
                onStabilizedListener.onStart();
            }
            // 1 prepare the input video and check it string input
            mFrameSource = new MyFrameSource(sourceVideo);

            // 2 prepare the motion estimator
            // first prepare the motion the estimation builder RANSAC L2;
            MotionEstimatorRansacL2 est = new MotionEstimatorRansacL2(mVizoVideoStabConfig.getMotionMode());

            // set ransac params for motion estimator
            RansacParams ransacParams = est.ransacParams();
            mVizoVideoStabConfig.setRansacParams(ransacParams);

            est.setRansacParams(ransacParams);
            est.setMinInlierRatio(mVizoVideoStabConfig.getMinInLierRatio());

            // seconds, create a feature detector
            GFTTDetector feature = GFTTDetector.create();

            // third, create the motion estimator
            KeypointBasedMotionEstimator motionEstBuilder = new KeypointBasedMotionEstimator(est);
            motionEstBuilder.setDetector(feature);

            // define error
            IOutlierRejector outlierRejector = new IOutlierRejector(new NullOutlierRejector());
            motionEstBuilder.setOutlierRejector(outlierRejector);

            // 3 - Prepare stabilizer
            TwoPassStabilizer stabilizer = new TwoPassStabilizer();

            // seconds, setup parameter
            stabilizer.setFrameSource(mFrameSource);
            stabilizer.setMotionEstimator(motionEstBuilder);
            stabilizer.setRadius(mVizoVideoStabConfig.getStabRadius());
            stabilizer.setTrimRatio(mVizoVideoStabConfig.getStabTrimRatio());
            stabilizer.setCorrectionForInclusion(mVizoVideoStabConfig.isInclusion());
            stabilizer.setBorderMode(mVizoVideoStabConfig.getBorderTypes().value());
            mFrameSource = stabilizer.asIFrameSource();
            mFrameSource.reset();

            // 4-Processing the stabilized frames. The results are showed and saved.
            processing(mFrameSource, destVideo);

        }catch (Exception e) {
            e.printStackTrace();
            mFrameSource.close();
        }
    }

    /**
     * <p>This method is created to process and stabilize each frame. This function needs to introduce a
     * path to save the resulting video ( string outputPath = ".//stabilizedVideo.avi" ) and set the
     * playback speed ( double outputFps = 25 ). </p>
     *
     * Afterwards, this function calculates each stabilized
     * frame until there areno more frames ( stabilizedFrame = stabilizedFrames-> nextFrame().empty() ).
     * Internally, the stabilizer first estimates the motion of every frame. This function creates a
     * video writer ( writer.open(outputPath,VideoWriter::fourcc('X','V','I','D'), outputFps,
     * stabilizedFrame.size()) ) to store each frame in the XVID format
     *
     * @param frameSource stabilize each frame
     * @param outpath a path to save the resulting video.
     */
    private void processing(IFrameSource frameSource, String outpath) {
        if (frameSource == null || outpath.isEmpty())
            throw new IllegalArgumentException("IFrameSource and Out Path can't be null");

        try {
            VideoWriter videoWriter = new VideoWriter();

            Log.d(">>>stab", "Started");
            Mat stabilizedFrame = null;
            int nFrames = 0;

            final int maxFrame = frameGrabber.getLengthInFrames();
            final int[] params = new int[]{opencv_imgcodecs.IMWRITE_JPEG_PROGRESSIVE, 0, opencv_imgcodecs.IMWRITE_JPEG_OPTIMIZE, 0, opencv_imgcodecs.IMWRITE_JPEG_QUALITY, 70};

            //for each stabilized frame
            while (nFrames < maxFrame) {
                stabilizedFrame = frameSource.nextFrame();
                if (stabilizedFrame != null && !stabilizedFrame.empty()) {
                    nFrames++;

                    if (!videoWriter.isOpened()) {
                        videoWriter.open(outpath + "/stab_video.avi",
                                VideoWriter.fourcc(
                                        ((byte) 'M'),
                                        ((byte) 'J'),
                                        ((byte) 'P'),
                                        ((byte) 'G')),
                                VizoVideoStabConfig.OUT_FPS,
                                new opencv_core.Size(stabilizedFrame.rows(), stabilizedFrame.cols()));

                    }

                    // rotate mat, because when capture video has rotated corresponding
                    // with the device orientation and font/back camera.
                    stabilizedFrame = rotate(rotateVideo, stabilizedFrame);

                    // resize image
                    Mat dest = new Mat();
                    //resize(outImagePrev, dest, new opencv_core.Size(mOutImgSize.getWidth(), mOutImgSize.getHeight()));

                    // save to disk
                    videoWriter.write(stabilizedFrame);

                    if (onStabilizedListener != null) {
                        onStabilizedListener.onProcess(nFrames, stabilizedFrame);
                    }
                    // release
                    stabilizedFrame.release();
                    dest.release();

                    Log.e(">>>IFrameSource", "Loop: " + nFrames);
                }
            }

            try {
                stabilizedFrame.release();
                frameGrabber.release();
            } catch (FrameGrabber.Exception e) {
                e.printStackTrace();

            } finally {
                if (onStabilizedListener != null) {
                    onStabilizedListener.onProcess(nFrames, stabilizedFrame);
                }
            }

            Log.d(">>>stab", "Processed Frames: " + nFrames);
            Log.d(">>>stab", "Finished");
        } catch (Exception ex){
            ex.printStackTrace();
        }
    }

    static class MyFrameSource extends IFrameSource {
        private FrameGrabber mFrameGrabber;
        private OpenCVFrameConverter.ToMat mConverter;

        MyFrameSource(String sourceVideo) {
            mFrameGrabber = new FFmpegFrameGrabber(sourceVideo);
            mConverter = new OpenCVFrameConverter.ToMat();
            start();
        }

        @Override
        public Mat nextFrame() {
            try {
                Log.e(">>>IFrameSource", "nextFrame");
                return mConverter.convert(mFrameGrabber.grabFrame());
            } catch (FrameGrabber.Exception e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        public void reset() {
            try {
                Log.e(">>>IFrameSource", "reset");
                mFrameGrabber.restart();
            } catch (FrameGrabber.Exception e) {
                e.printStackTrace();
            }
        }

        public void start() {
            try {
                mFrameGrabber.start();
            } catch (FrameGrabber.Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Remote {@link Mat} with angle if you want.
     *
     * @param angle is angle you want to rotate
     * @param frameMat {@link Mat} of the image.
     * @return Mat after rotated.
     */
    private Mat rotate(int angle, Mat frameMat) {
        opencv_core.Mat dest = new opencv_core.Mat(frameMat.cols(), frameMat.rows(), frameMat.type());
        if ((angle % 90) == 0 && angle <= 360 && angle >= -360) {
            // rotate clockwise 270 degrees
            if (angle == 270 || angle == -90) {
                transpose(frameMat, dest);
                flip(dest, dest, 0);
            }
            // rotate clockwise 180 degrees
            else if (angle == 180 || angle == -180){
                flip(frameMat, dest, -1);
            }
            // rotate clockwise 90 degrees
            else if (angle == 90 || angle == -270) {
                transpose(frameMat, dest);
                flip(dest, dest, 1);

            } else if (angle == 360 || angle == 0 || angle == -360) {
                if (frameMat.data() != dest.data()) {
                    frameMat.copyTo(dest);
                }
            }
        }

        return dest;
    }

    public void setSizeImage(opencv_core.Size sizeImage) {
        this.mOutImgSize = sizeImage;
    }
    public void setRotateVideo(int angle) {
        this.rotateVideo = angle;
    }
    public void setFrameGrabber(FFmpegFrameGrabber frameGrabber){
        this.frameGrabber = frameGrabber;
    }

    public void setOnStabilizedListener(OnStabilizedListener onStabilizedListener) {
        this.onStabilizedListener = onStabilizedListener;
    }
}
