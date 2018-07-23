# Video stabilization

Video stabilization refers to a family of methods used to reduce the blurring associated
with the motion of the camera. In other words, it compensates for any angular movement,
equivalent to yaw, pitch, roll, and x and y translations of the camera. The first image
stabilizers appeared in the early 60s. These systems were able to slightly compensate for
camera shakes and involuntary movements. They were controlled by gyroscopes and
accelerometers based on mechanisms that could cancel or reduce unwanted movement by
changing the position of a lens. Currently, these methods are widely used in binoculars,
video cameras, and telescopes.

There are various methods for image or video stabilization, and this sample focuses on the
most extended families of methods:

**Mechanical stabilization systems**: These systems use a mechanical system on the
camera lens so that when the camera is moved, motion is detected by accelerometers
and gyroscopes, and the system generates a movement on the lens. These systems
will not be considered here.

**Digital stabilization systems**: These are normally used in video and they act directly
on the image obtained from the camera. In these systems, the surface of the stabilized
image is slightly smaller than the source image’s surface. When the camera is moved,
the captured image is moved to compensate this movement. Although these
techniques effectively work to cancel movement by reducing the usable area of
movement sensor, resolution and image clarity are sacrificed.

Video-stabilization algorithms usually encompass the following steps:

**Source Video Frames** -> Motion Estimator -> **Matrices of Motion** -> Transformation of Frames -> **Stabilized Video Frames with some Detects** -> Video Completion, Debluring,etc. -> **Stabilized Video.**

You can find more detailed information about the RANSAC method at
https://en.wikipedia.org/wiki/Random_sample_consensus

The second step generates a new sequence of frames based on the estimated motion.
Additional processing is performed, such as smoothing, deblurring, border extrapolation,
and so on, to improve the quality of stabilization.

The third step removes the annoying irregular perturbations—refer to the following figure.
There are approaches that assume a camera-motion model, which work well when some
assumptions can be made about the actual camera motion.
