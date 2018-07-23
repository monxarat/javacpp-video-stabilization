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

import org.bytedeco.javacpp.opencv_videostab;
import org.bytedeco.javacpp.opencv_videostab.RansacParams;

/**
 * Created by chinhnq on 7/20/18.
 *
 * Create all config for video stabilization.
 */
public class VideoStabConfig {

    public static final float MIN_INLIER_RATIO = 0.1f;
    private float minInLierRatio = MIN_INLIER_RATIO;

    public static final int NKPS = 1000;
    public static final boolean TWO_PASS = true;

    /** radius using for gaussian filter */
    public static final int RADIUS_PASS = 15;

    /** trim frame after stabilized */
    public static final boolean EST_TRIM = true;

    public static final boolean INCLUSION = false;
    private boolean isInclusion = INCLUSION;

    public static final float TRIM_RATIO = 0.1f;
    public static final int STAB_RADIUS = 15;

    private int stabRadius = STAB_RADIUS;
    private float stabTrimRatio = TRIM_RATIO;

    public static final int OUT_FPS = 25;
    public String outVideoPath = null;


    private RansacParams ransacParams;

    /** RANSAC configs*/
    public static final int RANSAC_SIZE = 3;
    public static final int RANSAC_THRESH = 5;
    public static final float RANSAC_ESP = 0.5f;

    /** subset size */
    private int ransacSize = RANSAC_SIZE;

    /** max error to classify as inlier */
    private int ransacThresh = RANSAC_THRESH;

    /** max outliers ratio */
    private float ransacEPS = RANSAC_ESP;

    /** motion model */
    private int motionMode = opencv_videostab.MM_AFFINE;

    private BorderTypes borderTypes = BorderTypes.BORDER_REPLICATE;

    public VideoStabConfig() {
        ransacParams = new RansacParams();
    }

    public void setRansacParams(RansacParams ransacParams) {
        ransacParams.size(getRansacSize());
        ransacParams.thresh(getRansacThresh());
        ransacParams.eps(getRansacEPS());
        this.ransacParams = ransacParams;
    }

    public int getRansacSize() {
        return ransacSize;
    }

    public void setRansacSize(int ransacSize) {
        this.ransacSize = ransacSize;
    }

    public int getRansacThresh() {
        return ransacThresh;
    }

    public void setRansacThresh(int ransacThresh) {
        this.ransacThresh = ransacThresh;
    }

    public float getRansacEPS() {
        return ransacEPS;
    }

    public void setRansacEPS(float ransacEPS) {
        this.ransacEPS = ransacEPS;
    }

    public String getOutVideoPath() {
        return outVideoPath;
    }

    public void setOutVideoPath(String outVideoPath) {
        this.outVideoPath = outVideoPath;
    }

    public BorderTypes getBorderTypes() {
        return borderTypes;
    }

    public void setBorderTypes(BorderTypes borderTypes) {
        this.borderTypes = borderTypes;
    }

    public int getStabRadius() {
        return stabRadius;
    }

    public void setStabRadius(int stabRadius) {
        this.stabRadius = stabRadius;
    }

    public float getStabTrimRatio() {
        return stabTrimRatio;
    }

    public void setStabTrimRatio(float stabTrimRatio) {
        this.stabTrimRatio = stabTrimRatio;
    }

    public boolean isInclusion() {
        return isInclusion;
    }

    public void setInclusion(boolean inclusion) {
        isInclusion = inclusion;
    }

    public float getMinInLierRatio() {
        return minInLierRatio;
    }

    public void setMinInLierRatio(float minInLierRatio) {
        this.minInLierRatio = minInLierRatio;
    }

    public int getMotionMode() {
        return motionMode;
    }

    public void setMotionMode(int motionMode) {
        this.motionMode = motionMode;
    }
}
