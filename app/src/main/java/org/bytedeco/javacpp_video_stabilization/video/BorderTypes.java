/*
 * Copyright (C) 2018 Nguye Quoc Chinh
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

/**
 * Created by chinhnq on 7/12/18.
 *
 * Various border types, image boundaries are denoted with.
 * <p>
 *     The getGaussianKernel and getGaborKernel functions can be used in OpenCV to
 *     generate custom kernels, which can then be passed on to filter2D .
 *     In all cases, it is necessary to extrapolate the values of the non-existent pixels outside the
 *     image boundary. OpenCV enables the specification of the extrapolation method in most of
 *     the filter functions. These methods are:
 * </p>
 *
 * <ur>
 *     <li>{@link #BORDER_CONSTANT}</li>
 *     <li>{@link #BORDER_REPLICATE}</li>
 *     <li>{@link #BORDER_REFLECT}</li>
 *     <li>{@link #BORDER_WRAP}</li>
 *     <li>{@link #BORDER_REFLECT_101}</li>
 *     <li>{@link #BORDER_TRANSPARENT}</li>
 *     <li>{@link #BORDER_REFLECT101}</li>
 *     <li>{@link #BORDER_DEFAULT}</li>
 *     <li>{@link #BORDER_ISOLATED}</li>
 * </ur>
 *
 * @see  https://docs.opencv.org/3.4/d2/de8/group__core__array.html#ga209f2f4869e304c82d07739337eae7c5
 */
public enum BorderTypes {

    /**
     * This establishes a constant over the new border:
     * kkkkkk|abcdefgh|kkkkkk
     */
    BORDER_CONSTANT(0),

    /**
     * This repeats the last known pixel value:
     * aaaaaa|abcdefgh|hhhhhhh
     */
    BORDER_REPLICATE(1),

    /**
     * This reflects the image border:
     * fedcba|abcdefgh|hgfedcb
     */
    BORDER_REFLECT(2),

    /**
     * This appends the value of the opposite border:
     * cdefgh|abcdefgh|abcdefg
     */
    BORDER_WRAP(3),

    /**
     * This reflects the image border without duplicating the last pixel of the border:
     * gfedcb|abcdefgh|gfedcba
     */
    BORDER_REFLECT_101(4),
    BORDER_TRANSPARENT(5),
    BORDER_REFLECT101(BORDER_REFLECT_101),
    BORDER_DEFAULT(BORDER_REFLECT_101),
    BORDER_ISOLATED(16);

    private int bordertype;
    private BorderTypes border;

    BorderTypes(int borderType) {
        this.bordertype = borderType;
    }
    BorderTypes(BorderTypes borderType) {
        this.border = borderType;
    }

    public int value() {
        return bordertype;
    }
}
