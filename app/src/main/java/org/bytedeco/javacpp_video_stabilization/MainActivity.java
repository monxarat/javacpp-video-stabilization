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
package org.bytedeco.javacpp_video_stabilization;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import org.bytedeco.javacpp.opencv_core;
import org.bytedeco.javacpp_video_stabilization.video.VideoStabilization;
import org.bytedeco.javacv.FFmpegFrameGrabber;
import org.bytedeco.javacv.FrameGrabber;

/**
 * Created by chinhnq on 7/23/18.
 *
 * Example Video Stabilization
 */
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        String videoPath = "<Video Source path>";
        FFmpegFrameGrabber frameGrabber = new FFmpegFrameGrabber(videoPath);
        try {
            frameGrabber.start();

            VideoStabilization videoStab = new VideoStabilization(
                    videoPath,
                    "<Destination video path>");
            videoStab.setFrameGrabber(frameGrabber);
            videoStab.setSizeImage(new opencv_core.Size(720, 1280));

            videoStab.stabilizer();
        } catch (FrameGrabber.Exception e) {
            e.printStackTrace();
        }
    }
}
