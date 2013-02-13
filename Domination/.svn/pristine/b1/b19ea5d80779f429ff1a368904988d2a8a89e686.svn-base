package collisionphysics;

/**
 * This class provides static methods for collision detection and 
 * responses, based on Netwon's physics.
 * It is modeled after java.lang.Math.
 * 
 * The collision detection is based on ray tracing and vector analysis.
 * In all situations, we try to compute the parameter t (collision time),
 * and accept the minimum t, such that, 0 < t <= detectionTimeLimit.
 * 
 * In a complex system (e.g., many bouncing balls), only the first collision
 * matters. Hence, we need to find the earliest (smallest) t among all the
 * detected collisions.
 *
 * @author Hock-Chuan Chua
 * @version 0.3 (30 October 2010)
 */
 /* 
  * TODO: 3D support
  * TODO: Error Analysis and Test cases
  * TODO: assert in public methods
  */
public class CollisionPhysics {

   // Working copy for computing response in intersect(ContainerBox box), 
   // to avoid repeatedly allocating objects.
   private static CollisionResponse tempResponse = new CollisionResponse(); 
   
   /**
    * Detect collision for a moving point bouncing inside a rectangular container,
    * within the given timeLimit.
    * If collision is detected within the timeLimit, compute collision time and 
    * response in the given CollisionResponse object. Otherwise, set collision time
    * to infinity.
    * The result is passed back in the given CollisionResponse object.
    * 
    * @param pointX : x-position of the center of the point.
    * @param pointY : y-position of the center of the point.
    * @param speedX : speed in x-direction.
    * @param speedY : speed in y-direction.
    * @param radius : radius of the point. Zero for a true point.
    * @param rectX1 : top-left corner x of the rectangle 
    * @param rectY1 : top-left corner y of the rectangle
    * @param rectX2 : bottom-right corner x of the rectangle
    * @param rectY2 : bottom-right corner y of the rectangle
    * @param timeLimit : max time to detect collision, in (0, 1] range.
    * @param response : If collision is detected, update the collision time and response.
    *                   Otherwise, set collision time to infinity.
    */
   public static void pointIntersectsRectangleOuter(
         float pointX, float pointY, float speedX, float speedY, float radius,
         float rectX1, float rectY1, float rectX2, float rectY2,
         float timeLimit, CollisionResponse response) {
      
      // Assumptions:
      assert (rectX1 < rectX2) && (rectY1 < rectY2): "Malformed rectangle!";
      assert (pointX >= rectX1 + radius) && (pointX <= rectX2 - radius) 
            && (pointY >= rectY1 + radius) && (pointY <= rectY2 - radius)
            : "Point (with radius) is outside the rectangular container!";
      assert (radius >= 0) : "Negative radius!";
      assert (timeLimit > 0) : "Non-positive time";

      response.reset();  // Reset detected collision time to infinity
      
      // A outer rectangular container box has 4 borders. 
      // Need to look for the earliest collision, if any.

      // Right border
      pointIntersectsLineVertical(pointX, pointY, speedX, speedY, radius,
            rectX2, timeLimit, tempResponse);
      if (tempResponse.t < response.t) {
         response.copy(tempResponse);  // Copy into resultant response
      }
      // Left border
      pointIntersectsLineVertical(pointX, pointY, speedX, speedY, radius,
            rectX1, timeLimit, tempResponse);
      if (tempResponse.t < response.t) {
         response.copy(tempResponse);  // Copy into resultant response
      }
      // Top border
      pointIntersectsLineHorizontal(pointX, pointY, speedX, speedY, radius,
            rectY1, timeLimit, tempResponse);
      if (tempResponse.t < response.t) {
         response.copy(tempResponse);  // Copy into resultant response
      }
      // Bottom border
      pointIntersectsLineHorizontal(pointX, pointY, speedX, speedY, radius,
            rectY2, timeLimit, tempResponse);
      if (tempResponse.t < response.t) {
         response.copy(tempResponse);  // Copy into resultant response
      }

      // FIXME: What if two collisions at the same time??
      // The CollisionResponse object keeps the result of one collision, not both! 
   }
   
   /**
    * Detect collision for a moving point hitting a vertical line,
    * within the given timeLimit.
    * If collision is detected within the timeLimit, compute collision time and 
    * response in the given CollisionResponse object. Otherwise, set collision time
    * to infinity.
    * The result is passed back in the given CollisionResponse object.
    * 
    * @param pointX : x-position of the center of the point.
    * @param pointY : y-position of the center of the point.
    * @param speedX : speed in x-direction.
    * @param speedY : speed in y-direction.
    * @param radius : radius of the point. Zero for a true point.
    * @param lineX : x-value of the vertical line
    * @param timeLimit : max time to detect collision, in (0, 1] range.
    * @param response : If collision is detected, update the collision time and response.
    *                   Otherwise, set collision time to infinity.
    */
   public static void pointIntersectsLineVertical(
         float pointX, float pointY, float speedX, float speedY, float radius,
         float lineX, float timeLimit, CollisionResponse response) {

      // Assumptions:
      assert (radius >= 0) : "Negative radius!";
      assert (timeLimit > 0) : "Non-positive time";

      response.reset();  // Reset detected collision time to infinity

      // No collision possible if speedX is zero
      if (speedX == 0) { // FIXME: Should I use a threshold?
         return;
      }

      // Compute the distance to the line, offset by radius.
      float distance;
      if (lineX > pointX) {
         distance = lineX - pointX - radius; 
      } else {
         distance = lineX - pointX + radius; 
      }
      
      float t = distance / speedX;  // speedX != 0
      // Accept 0 < t <= timeLimit
      if (t > 0 && t <= timeLimit) {
         response.t = t;
         response.newSpeedX = -speedX;  // Reflect horizontally
         response.newSpeedY = speedY;   // No change vertically
      }
      
      // Error analysis:
      // nextX = lineX, which falls on the line. It never crosses the line. 
      // In next step t = 0 which is excluded from the acceptable t.
   }

   /**
    * @see movingPointIntersectsLineVertical().
    */
   public static void pointIntersectsLineHorizontal(
         float pointX, float pointY, float speedX, float speedY, float radius,
         float lineY, float timeLimit, CollisionResponse response) {

      // Assumptions:
      assert (radius >= 0) : "Negative radius!";
      assert (timeLimit > 0) : "Non-positive time";

      response.reset();  // Reset detected collision time to infinity

      // No collision possible if speedY is zero
      if (speedY == 0) { // Should I use a threshold?
         return;
      }

      // Compute the distance to the line, offset by radius.
      float distance;
      if (lineY > pointY) {
         distance = lineY - pointY - radius; 
      } else {
         distance = lineY - pointY + radius; 
      }
      
      float t = distance / speedY;  // speedY != 0
      // Accept 0 < t <= timeLimit
      if (t > 0 && t <= timeLimit) {
         response.t = t;
         response.newSpeedY = -speedY;  // Reflect vertically
         response.newSpeedX = speedX;   // No change horizontally
      }
   }
   
   /**
    * Detect collision for a moving point bouncing inside a circular container,
    * within the given timeLimit.
    * If collision is detected within the timeLimit, compute collision time and 
    * response in the given CollisionResponse object. Otherwise, set collision time
    * to infinity.
    * The result is passed back in the given CollisionResponse object.
    * 
    * @param pointX : x-position of the center of the point.
    * @param pointY : y-position of the center of the point.
    * @param speedX : speed in x-direction.
    * @param speedY : speed in y-direction.
    * @param radius : radius of the point. Zero for a true point.
    * @param outerCenterX : center y of the circular container.
    * @param outerCenterY : center x of the circular container.
    * @param outerRadius : radius of the circular container.
    * @param timeLimit : max time to detect collision, in (0, 1] range.
    * @param response : If collision is detected, update the collision time and response.
    *                   Otherwise, set collision time to infinity.
    */
   public static void pointIntersectsCircleOuter(
         float pointX, float pointY, float speedX, float speedY, float radius,
         float outerCenterX, float outerCenterY, float outerRadius,
         float timeLimit, CollisionResponse response) {

      // Assumptions:
      assert (radius >= 0 && outerRadius >= 0) : "Negative radius!";
      assert (timeLimit > 0) : "Non-positive time";
      // Must the point be inside?
      // radius < outerRadius?

      response.reset();  // Set detected collision time to infinity

      // Call helper method to compute the collision time t
      float t = pointIntersectsCircleOuterDetection(
            pointX, pointY, speedX, speedY, radius,
            outerCenterX, outerCenterY, outerRadius);
      // Accept 0 < t <= timeLimit
      if (t > 0 && t <= timeLimit) {
         
         // Get the point of impact to form the line of collision
         float impactX = pointX + speedX * t;
         float impactY = pointY + speedY * t;
         
         // Call helper method to compute the response
         pointIntersectsLineNormalResponse(
               pointX, pointY, speedX, speedY, 
               outerCenterX, outerCenterY, impactX, impactY,
               response, t);
      }
   }
   
   /**
    * Helper method to compute and return the collision time, or infinity if
    * collision is no possible.
    * 
    * @param pointX : x-position of the center of the point.
    * @param pointY : y-position of the center of the point.
    * @param speedX : speed in x-direction.
    * @param speedY : speed in y-direction.
    * @param radius : radius of the point. Zero for a true point.
    * @param outerCenterX : center y of the circular container.
    * @param outerCenterY : center x of the circular container.
    * @param outerRadius : radius of the circular container.
    * @return : detected collision time or infinity if no collision detected.
    */
   private static float pointIntersectsCircleOuterDetection(
         float pointX, float pointY, float speedX, float speedY, float radius,
         float outerCenterX, float outerCenterY, float outerRadius) {
      
      // Rearrange the parameters to form the quadratic equation
      double offsetPointX = pointX - outerCenterX;
      double offsetPointY = pointY - outerCenterY;
      double effectiveRadius = outerRadius - radius;
      double sqSpeedX = speedX * speedX;
      double sqSpeedY = speedY * speedY;
      double sqEffectiveRadius = effectiveRadius * effectiveRadius;
      double sqOffsetPointX = offsetPointX * offsetPointX;
      double sqOffsetPointY = offsetPointY * offsetPointY;
      
      double termA = sqSpeedX + sqSpeedY;
      double termB = 2.0 * (speedX * offsetPointX + speedY * offsetPointY);
      double termC = sqOffsetPointX + sqOffsetPointY - sqEffectiveRadius;
      double b2minus4ac = termB * termB - 4.0 * termA * termC;  
      if (b2minus4ac < 0) {
         return Float.MAX_VALUE;
      }
      double rootB2minus4ac = Math.sqrt(b2minus4ac);
      double sol1 = (-termB + rootB2minus4ac) / (2.0 * termA);
      double sol2 = (-termB - rootB2minus4ac) / (2.0 * termA);
      // return the smaller positive solution
      if (sol1 > 0 && sol2 > 0) {
         return (float)Math.min(sol1, sol2);
      } else if (sol1 > 0) {
         return (float)sol1;
      } else if (sol2 > 0) {
         return (float)sol2;
      } else {
         return Float.MAX_VALUE;
      }
   }
   
   /**
    * Helper method to compute the collision response given collision time (t)
    * for a moving point and a line. The line is specified by its normal.
    * Store and return the result in the given CollisionResponse object.
    * 
    * @param pointX : x-position of the center of the point.
    * @param pointY : y-position of the center of the point.
    * @param speedX : speed in x-direction.
    * @param speedY : speed in y-direction.
    * @param lineNormalX1 : x1 of the normal to the collision line.
    * @param lineNormalY1 : y1 of the normal to the collision line.
    * @param lineNormalX2 : x2 of the normal to the collision line.
    * @param lineNormalY2 : y2 of the normal to the collision line.
    * @param response : updated the collision time and response.
    * @param t : the given detected collision time.
    */
   private static void pointIntersectsLineNormalResponse(
         float pointX, float pointY, float speedX, float speedY, 
         float lineNormalX1, float lineNormalY1, float lineNormalX2, float lineNormalY2,
         CollisionResponse response, float t) {

      response.t = t;

      // Direction along the line normal is N, perpendicular is P.
      // Project velocity from (x, y) to (n, p)
      double lineAngle = Math.atan2(lineNormalY2 - lineNormalY1, lineNormalX2 - lineNormalX1);
      double[] result = rotate(speedX, speedY, lineAngle);
      double speedN = result[0];
      double speedP = result[1];

      // Reflect along the normal (N), no change along the line of collision (P).
      double speedNAfter = -speedN;
      double speedPAfter = speedP;

      // Project back from (n, p) to (x, y)
      result = rotate(speedNAfter, speedPAfter, -lineAngle);
      response.newSpeedX = (float)result[0];
      response.newSpeedY = (float)result[1];
   }

   /**
    * Detect collision for a moving point hitting a arbitrary polygon,
    * within the given timeLimit.
    * If collision is detected within the timeLimit, compute collision time and 
    * response in the given CollisionResponse object. Otherwise, set collision time
    * to infinity.
    * The result is passed back in the given CollisionResponse object.
    * 
    * @param pointX : x-position of the center of the point.
    * @param pointY : y-position of the center of the point.
    * @param speedX : speed in x-direction.
    * @param speedY : speed in y-direction.
    * @param radius : radius of the point. Zero for a true point.
    * @param polygonXs : array of x points of the polygon.
    * @param polygonYx : array of y points of the polygon.
    * @param numPoints : number of points to be taken from the point array.
    * @param timeLimit : max time to detect collision, in (0, 1] range.
    * @param response : If collision is detected, update the collision time and response.
    *                   Otherwise, set collision time to infinity.
    */
   public static void pointIntersectsPolygon(
         float pointX, float pointY, float speedX, float speedY, float radius,
         int[] polygonXs, int[] polygonYs, int numPoints,
         float timeLimit, CollisionResponse response) {
      
      // Assumptions:
      assert (radius >= 0) : "Negative radius!";
      assert (timeLimit > 0) : "Non-positive time";
      // numPoints: 0? 1? 2?

      response.reset();  // Set the collision time to infinity

      // Check probable collision with each of the line segments.
      // Update the earliest collision.
      int lineX1, lineX2, lineY1, lineY2;
      for (int segment = 0; segment < numPoints; segment++) {
         lineX1 = polygonXs[segment];
         lineY1 = polygonYs[segment];
         lineX2 = polygonXs[(segment + 1) % numPoints];
         lineY2 = polygonYs[(segment + 1) % numPoints];
         CollisionPhysics.pointIntersectsLineSegmentNoEndPoints(
               pointX, pointY, speedX, speedY, radius, 
               lineX1, lineY1, lineX2, lineY2,
               timeLimit, tempResponse);
         if (tempResponse.t < response.t) {
            response.copy(tempResponse);
         }
      }
      // Check each of the points that made up the polygon.
      for (int i = 0; i < numPoints; i++) {
         CollisionPhysics.pointIntersectsPoint(
               pointX, pointY, speedX, speedY, radius, 
               polygonXs[i], polygonYs[i], 0,
               timeLimit, tempResponse);
         if (tempResponse.t < response.t) {
            response.copy(tempResponse);
         }
      }
   }
   
   /**
    * Detect collision for a moving point hitting hitting an arbitrary line,
    * within the given timeLimit.
    * If collision is detected within the timeLimit, compute collision time and 
    * response in the given CollisionResponse object. Otherwise, set collision time
    * to infinity.
    * The result is passed back in the given CollisionResponse object.
    * 
    * @param pointX : x-position of the center of the point.
    * @param pointY : y-position of the center of the point.
    * @param speedX : speed in x-direction.
    * @param speedY : speed in y-direction.
    * @param radius : radius of the point. Zero for a true point.
    * @param lineX1 : line's beginning point x value.
    * @param lineY1 : line's beginning point y value.
    * @param lineX2 : line's ending point x value.
    * @param lineY2 : line's ending point y value.
    * @param timeLimit : max time to detect collision, in (0, 1] range.
    * @param response : If collision is detected, update the collision time and response.
    *                   Otherwise, set collision time to infinity.
    */
   public static void pointIntersectsLine(
         float pointX, float pointY, float speedX, float speedY, float radius,
         float lineX1, float lineY1, float lineX2, float lineY2,
         float timeLimit, CollisionResponse response) {

      // Assumptions:
      assert (radius >= 0) : "Negative radius!";
      assert (timeLimit > 0) : "Non-positive time";
      // lineX1 == lineX2 && lineY1 == lineY2, a point?

      // If line is vertical or horizontal, use simplified solution.
      if (lineX1 == lineX2) {  // Vertical line
         pointIntersectsLineVertical(pointX, pointY, speedX, speedY, radius,
               lineX1, timeLimit, response);
         return;
      } else if (lineY1 == lineY2) {  // Horizontal line
         pointIntersectsLineHorizontal(pointX, pointY, speedX, speedY, radius,
               lineY1, timeLimit, response);
         return;
      }

      response.reset();  // Set collision time to infinity

      // Call helper method to compute the collision time.
      float t = pointIntersectsLineDetection(
            pointX, pointY, speedX, speedY, radius,
            lineX1, lineY1, lineX2, lineY2)[0];
      // Accept 0 < t <= timeLimit
      if (t > 0 && t <= timeLimit) {
         // Call helper method to compute the response in Response object
         pointIntersectsLineResponse(
               pointX, pointY, speedX, speedY, 
               lineX1, lineY1, lineX2, lineY2, response, t);
      }
   }

   // The solution for colliding to a line has tow parts: t and lambda.
   // Re-use this to avoid repeatedly allocating new instances.
   private static float[] pointLineResult = new float[2];
   /**
    * Helper method to compute the collision time (t) and point of impact
    * on the line (lambda), for a moving point and a line.
    * 
    * @param pointX : x-position of the center of the point.
    * @param pointY : y-position of the center of the point.
    * @param speedX : speed in x-direction.
    * @param speedY : speed in y-direction.
    * @param radius : radius of the point. Zero for a true point.
    * @param lineX1 : line's beginning point x value.
    * @param lineY1 : line's beginning point y value.
    * @param lineX2 : line's ending point x value.
    * @param lineY2 : line's ending point y value.
    * @return an float[2], where
    *   First element is t, or infinity if no collision detected. 
    *   Second element is lambda, point of impact on the line. 
    */
   private static float[] pointIntersectsLineDetection(
         float pointX, float pointY, float speedX, float speedY, float radius,
         float lineX1, float lineY1, float lineX2, float lineY2) {

      double lineVectorX = lineX2 - lineX1;
      double lineVectorY = lineY2 - lineY1;

      // Compute the offset caused by radius
      double lineX1Offset = lineX1;
      double lineY1Offset = lineY1;
      
      // FIXME: Inefficient!
      if (radius > 0) {
         // Check which side of the line the point is. Offset reduces the distance
         double lineAngle = Math.atan2(lineVectorY, lineVectorX);
         double rotatedY = rotate(pointX - lineX1, pointY - lineY1, lineAngle)[1];
         if (rotatedY >  0) {
            lineX1Offset -= radius * Math.sin(lineAngle);
            lineY1Offset += radius * Math.cos(lineAngle);
         } else {
            lineX1Offset += radius * Math.sin(lineAngle);
            lineY1Offset -= radius * Math.cos(lineAngle);
         }
      }
      
      // Solve for t (time of collision) and lambda (point of impact on the line)
      double t;
      double lambda;
      double det = -speedX * lineVectorY + speedY * lineVectorX;

      if (det == 0) {             // FIXME: Use a threshold?
         t = Double.MAX_VALUE;    // No collision possible.
         lambda = Double.MAX_VALUE;
      }

      double xDiff = lineX1Offset - pointX;
      double yDiff = lineY1Offset - pointY;
      t = (-lineVectorY * xDiff + lineVectorX * yDiff) / det;
      lambda = (-speedY * xDiff + speedX * yDiff) / det;

      pointLineResult[0] = (float)t;
      pointLineResult[1] = (float)lambda;
      return pointLineResult;
   }

   /**
    * Helper method to compute the collision response given collision time (t)
    * for a moving point and a line.
    * Store and return the result in the given CollisionResponse object.
    * 
    * @param pointX : x-position of the center of the point.
    * @param pointY : y-position of the center of the point.
    * @param speedX : speed in x-direction.
    * @param speedY : speed in y-direction.
    * @param lineX1 : line's beginning point x value.
    * @param lineY1 : line's beginning point y value.
    * @param lineX2 : line's ending point x value.
    * @param lineY2 : line's ending point y value.
    * @param response : update collision time and response.
    * @param t : the given detected collision time
    */
   private static void pointIntersectsLineResponse(
         float pointX, float pointY, float speedX, float speedY, 
         float lineX1, float lineY1, float lineX2, float lineY2,
         CollisionResponse response, float t) {
      
      response.t = t;
      
      // Direction along the line of collision is P, normal is N.
      // Project velocity from (x, y) to (p, n)
      double lineAngle = Math.atan2(lineY2 - lineY1, lineX2 - lineX1);
      double[] result = rotate(speedX, speedY, lineAngle);
      double speedP = result[0];
      double speedN = result[1];

      // Reflect along the normal (N), no change along the line of collision (P)
      double speedPAfter = speedP;
      double speedQAfter = -speedN;

      // Project back from (p, n) to (x, y)
      result = rotate(speedPAfter, speedQAfter, -lineAngle);
      response.newSpeedX = (float)result[0];
      response.newSpeedY = (float)result[1];
   }

   /**
    * Detect collision for a moving point hitting hitting an arbitrary line segment,
    * within the given timeLimit. No need to consider the two end points.
    * If collision is detected within the timeLimit, compute collision time and 
    * response in the given CollisionResponse object. Otherwise, set collision time
    * to infinity.
    * The result is passed back in the given CollisionResponse object.
    * 
    * @param pointX : x-position of the center of the point.
    * @param pointY : y-position of the center of the point.
    * @param speedX : speed in x-direction.
    * @param speedY : speed in y-direction.
    * @param radius : radius of the point. Zero for a true point.
    * @param lineX1 : line's beginning point x value.
    * @param lineY1 : line's beginning point y value.
    * @param lineX2 : line's ending point x value.
    * @param lineY2 : line's ending point y value.
    * @param timeLimit : max time to detect collision, in (0, 1] range.
    * @param response : If collision is detected, update the collision time and response.
    *                   Otherwise, set collision time to infinity.
    */
   public static void pointIntersectsLineSegmentNoEndPoints(
         float pointX, float pointY, float speedX, float speedY, float radius,
         float lineX1, float lineY1, float lineX2, float lineY2,
         float timeLimit, CollisionResponse response) {
      
      // Assumptions:
      assert (radius >= 0) : "Negative radius!";
      assert (timeLimit > 0) : "Non-positive time";
      // lineX1 == lineX2 && lineY1 == lineY2, a point?

      // If line is vertical or horizontal, use simplified solution.
      if (lineX1 == lineX2) {  // Vertical line
         pointIntersectsLineVertical(pointX, pointY, speedX, speedY, radius,
               lineX1, timeLimit, response);
         // Need to confirm that the point of impact is within the line-segment
         double impactY = response.getImpactY(pointY, speedY);
         if (!(impactY >= lineY1 && impactY <= lineY2 || impactY >= lineY2 && impactY <= lineY1)) {
            response.reset();  // no collision
         }
         return;
      } else if (lineY1 == lineY2) {  // Horizontal line
         pointIntersectsLineHorizontal(pointX, pointY, speedX,
              speedY, radius, lineY1, timeLimit, response);
         // Need to confirm that the point of impact is within the line-segment
         double impactX = response.getImpactX(pointX, speedX);
         if (!(impactX >= lineX1 && impactX <= lineX2 || impactX >= lineX2 && impactX <= lineX1)) {
            response.reset();
         }
         return;
      }

      response.reset();  // Set detected collision time to infinity

      // Call helper method to compute the collision time.
      float[] result = pointIntersectsLineDetection(
            pointX, pointY, speedX, speedY, radius,
            lineX1, lineY1, lineX2, lineY2);
      float t = result[0];
      float lambda = result[1];
      
      // Accept 0 < t <= timeLimit
      if (t > 0 && t <= timeLimit && lambda >=0 && lambda <= 1) {
         // Call helper method to compute response.
         pointIntersectsLineResponse(pointX, pointY, speedX, speedY, 
                  lineX1, lineY1, lineX2, lineY2, response, t);
      }
   }
   
   /**
    * Detect collision for a moving point hitting hitting an arbitrary line segment,
    * within the given timeLimit. Consider both the end points.
    * If collision is detected within the timeLimit, compute collision time and 
    * response in the given CollisionResponse object. Otherwise, set collision time
    * to infinity.
    * The result is passed back in the given CollisionResponse object.
    * 
    * @param pointX : x-position of the center of the point.
    * @param pointY : y-position of the center of the point.
    * @param speedX : speed in x-direction.
    * @param speedY : speed in y-direction.
    * @param radius : radius of the point. Zero for a true point.
    * @param lineX1 : line's beginning point x value.
    * @param lineY1 : line's beginning point y value.
    * @param lineX2 : line's ending point x value.
    * @param lineY2 : line's ending point y value.
    * @param timeLimit : max time to detect collision, in (0, 1] range.
    * @param response : If collision is detected, update the collision time and response.
    *                   Otherwise, set collision time to infinity.
    */
   public static void pointIntersectsLineSegment(
         float pointX, float pointY, float speedX, float speedY, float radius,
         float lineX1, float lineY1, float lineX2, float lineY2,
         float timeLimit, CollisionResponse response) {
      
      // Assumptions:
      assert (radius >= 0) : "Negative radius!";
      assert (timeLimit > 0) : "Non-positive time";
      // lineX1 == lineX2 && lineY1 == lineY2, a point?
      
      response.reset();  // Reset the resultant response for earliest collision

      // Check the line segment for probable collision.
      CollisionPhysics.pointIntersectsLineSegmentNoEndPoints(
            pointX, pointY, speedX, speedY, radius, 
            lineX1, lineY1, lineX2, lineY2,
            timeLimit, tempResponse);
      if (tempResponse.t < response.t) {
         response.copy(tempResponse);
      }
      // Check the two end points (with radius = 0) for probable collision
      CollisionPhysics.pointIntersectsPoint(
            pointX, pointY, speedX, speedY, radius,
            lineX1, lineY1, 0,
            timeLimit, tempResponse);
      if (tempResponse.t < response.t) {
         response.copy(tempResponse);
      }
      CollisionPhysics.pointIntersectsPoint(
            pointX, pointY, speedX, speedY, radius,
            lineX2, lineY2, 0,
            timeLimit, tempResponse);
      if (tempResponse.t < response.t) {
         response.copy(tempResponse);
      }
   }
   
   /**
    * Detect collision for a moving point hitting another moving point,
    * within the given timeLimit.
    * If collision is detected within the timeLimit, compute collision time and 
    * response in the given CollisionResponse object. Otherwise, set collision time
    * to infinity.
    * The result is passed back in the given CollisionResponse object.
    * 
    * @param p1X : x-position of the center of point p1.
    * @param p1Y : y-position of the center of point p1.
    * @param p1SpeedX : p1's speed in x-direction.
    * @param p1SpeedY : p1's speed in y-direction.
    * @param p1Radius : p1's radius. Zero for a true point.
    * @param p2X : x-position of the center of point p2.
    * @param p2Y : y-position of the center of point p2.
    * @param p2SpeedX : p2's speed in x-direction.
    * @param p2SpeedY : p2's speed in y-direction.
    * @param p2Radius : p2's radius. Zero for a true point.
    * @param timeLimit : max time to detect collision, in (0, 1] range.
    * @param p1Response : If collision is detected, update the collision time and response for p1.
    *                   Otherwise, set collision time to infinity.
    * @param p2Response : If collision is detected, update the collision time and response for p2.
    *                   Otherwise, set collision time to infinity.
    */
   public static void pointIntersectsMovingPoint(
         float p1X, float p1Y, float p1SpeedX, float p1SpeedY, float p1Radius, 
         float p2X, float p2Y, float p2SpeedX, float p2SpeedY, float p2Radius,
         float timeLimit, CollisionResponse p1Response, CollisionResponse p2Response) {
      
      // Assumptions:
      assert (p1Radius >= 0) && (p2Radius >= 0) : "Negative radius!";
      assert timeLimit > 0 : "Non-positive time!";
      
      p1Response.reset();  // Set detected collision time to infinity
      p2Response.reset();
      
      // Call helper method to compute the collision time t.
      float t = pointIntersectsMovingPointDetection(    
            p1X, p1Y, p1SpeedX, p1SpeedY, p1Radius, 
            p2X, p2Y, p2SpeedX, p2SpeedY, p2Radius);

      // Accept 0 < t <= timeLimit 
      if (t > 0 && t <= timeLimit) {
         // Call helper method to compute the responses in the 2 Response objects
         pointIntersectsMovingPointResponse(
               p1X, p1Y, p1SpeedX, p1SpeedY, p1Radius, 
               p2X, p2Y, p2SpeedX, p2SpeedY, p2Radius,
               p1Response, p2Response, t);
      }
   }

   /**
    * Helper method to detect the collision time (t) for two moving points.
    * 
    * @param p1X : x-position of the center of point p1.
    * @param p1Y : y-position of the center of point p1.
    * @param p1SpeedX : p1's speed in x-direction.
    * @param p1SpeedY : p1's speed in y-direction.
    * @param p1Radius : p1's radius. Zero for a true point.
    * @param p2X : x-position of the center of point p2.
    * @param p2Y : y-position of the center of point p2.
    * @param p2SpeedX : p2's speed in x-direction.
    * @param p2SpeedY : p2's speed in y-direction.
    * @param p2Radius : p2's radius. Zero for a true point.
    * @return smaller positive t, or infinity if collision is not possible.
    */
   private static float pointIntersectsMovingPointDetection(
         float p1X, float p1Y, float p1SpeedX, float p1SpeedY, float p1Radius, 
         float p2X, float p2Y, float p2SpeedX, float p2SpeedY, float p2Radius) {
      
      // Rearrange the parameters to set up the quadratic equation.
      double centerX = p1X - p2X;
      double centerY = p1Y - p2Y;
      double speedX = p1SpeedX - p2SpeedX;
      double speedY = p1SpeedY - p2SpeedY;
      double radius = p1Radius + p2Radius;
      double radiusSq = radius * radius;
      double speedXSq = speedX * speedX;
      double speedYSq = speedY * speedY;
      double speedSq = speedXSq + speedYSq;
   
      // Solve quadratic equation for collision time t
      double termB2minus4ac = radiusSq * speedSq - (centerX * speedY - centerY * speedX)
            * (centerX * speedY - centerY * speedX);
      if (termB2minus4ac < 0) {
         // No intersection.
         // Moving spheres may cross at different times, or move in parallel.
         return Float.MAX_VALUE;
      }
      
      double termMinusB = -speedX * centerX - speedY * centerY;
      double term2a = speedSq;
      double rootB2minus4ac = Math.sqrt(termB2minus4ac);
      double sol1 = (termMinusB + rootB2minus4ac) / term2a;
      double sol2 = (termMinusB - rootB2minus4ac) / term2a;
      // Accept the smallest positive t as the solution.
      if (sol1 > 0 && sol2 > 0) {
         return (float)Math.min(sol1, sol2);
      } else if (sol1 > 0) {
         return (float)sol1;
      } else if (sol2 > 0) {
         return (float)sol2;
      } else {
         // No positive t solution. Set detected collision time to infinity.
         return Float.MAX_VALUE;
      }
   }

   /**
    * Helper method to compute the collision response given the collision time (t),
    * for two moving points.
    * Store and return the results in the two given CollisionResponse objects.
    * 
    * @param p1X : x-position of the center of point p1.
    * @param p1Y : y-position of the center of point p1.
    * @param p1SpeedX : p1's speed in x-direction.
    * @param p1SpeedY : p1's speed in y-direction.
    * @param p1Radius : p1's radius. Zero for a true point.
    * @param p2X : x-position of the center of point p2.
    * @param p2Y : y-position of the center of point p2.
    * @param p2SpeedX : p2's speed in x-direction.
    * @param p2SpeedY : p2's speed in y-direction.
    * @param p2Radius : p2's radius. Zero for a true point.
    * @param p1Response : To update the collision time and response for p1.
    *                     Reset time to infinity if error is detected.
    * @param p2Response : To update the collision time and response for p2.
    *                     Reset time to infinity if error is detected.
    * @param t : the given detected collision time.
    */
   private static void pointIntersectsMovingPointResponse(
         float p1X, float p1Y, float p1SpeedX, float p1SpeedY, float p1Radius, 
         float p2X, float p2Y, float p2SpeedX, float p2SpeedY, float p2Radius,
         CollisionResponse p1Response, CollisionResponse p2Response, float t) {

      // Update the detected collision time in CollisionResponse.
      p1Response.t = t; 
      p2Response.t = t;

      // Get the point of impact, to form the line of collision.
      double p1ImpactX = p1Response.getImpactX(p1X, p1SpeedX); 
      double p1ImpactY = p1Response.getImpactY(p1Y, p1SpeedY); 
      double p2ImpactX = p2Response.getImpactX(p2X, p2SpeedX); 
      double p2ImpactY = p2Response.getImpactY(p2Y, p2SpeedY); 
      
      // Direction along the line of collision is P, normal is N.
      // Get the direction along the line of collision
      double lineAngle = Math.atan2(p2ImpactY - p1ImpactY, p2ImpactX - p1ImpactX);

      // Project velocities from (x, y) to (p, n) 
      double[] result = rotate(p1SpeedX, p1SpeedY, lineAngle);
      double p1SpeedP = result[0];
      double p1SpeedN = result[1];
      result = rotate(p2SpeedX, p2SpeedY, lineAngle);
      double p2SpeedP = result[0];
      double p2SpeedN = result[1];

      // Collision possible only if p1SpeedP - p2SpeedP > 0
      // Needed if the two balls overlap in their initial positions
      // Do not declare collision, so that they continue their course of movement
      // until they are separated.
      if (p1SpeedP - p2SpeedP <= 0) {
         //System.out.println("velocities cannot collide! t = " + t);
         p1Response.reset();  // Set collision time to infinity
         p2Response.reset();
         return;
      }
      
      // Assume that mass is proportional to the cube of radius. 
      // (All objects have the same density.)
      double p1Mass = p1Radius * p1Radius * p1Radius;
      double p2Mass = p2Radius * p2Radius * p2Radius;
      double diffMass = p1Mass - p2Mass;
      double sumMass = p1Mass + p2Mass;

      double p1SpeedPAfter, p1SpeedNAfter, p2SpeedPAfter, p2SpeedNAfter;
      // Along the collision direction P, apply conservation of energy and momentum
      p1SpeedPAfter = (diffMass * p1SpeedP + 2.0 * p2Mass * p2SpeedP) / sumMass;
      p2SpeedPAfter = (2.0 * p1Mass * p1SpeedP - diffMass * p2SpeedP) / sumMass;

      // No change in the perpendicular direction N
      p1SpeedNAfter = p1SpeedN;
      p2SpeedNAfter = p2SpeedN;

      // Project the velocities back from (p, n) to (x, y)
      result = rotate(p1SpeedPAfter, p1SpeedNAfter, -lineAngle);
      p1Response.newSpeedX = (float)result[0];
      p1Response.newSpeedY = (float)result[1];
      result = rotate(p2SpeedPAfter, p2SpeedNAfter, -lineAngle);
      p2Response.newSpeedX = (float)result[0];
      p2Response.newSpeedY = (float)result[1];
   }
   
   /**
    * Detect collision for a moving point hitting a stationary point,
    * within the given timeLimit.
    * If collision is detected within the timeLimit, compute collision time and 
    * response in the given CollisionResponse object. Otherwise, set collision time
    * to infinity.
    * The result is passed back in the given CollisionResponse object.
    * 
    * @param p1X : x-position of the center of point p1.
    * @param p1Y : y-position of the center of point p1.
    * @param p1SpeedX : p1's speed in x-direction.
    * @param p1SpeedY : p1's speed in y-direction.
    * @param p1Radius : p1's radius. Zero for a true point.
    * @param p2X : x-position of the center of point p2.
    * @param p2Y : y-position of the center of point p2.
    * @param p2Radius : p2's radius. Zero for a true point.
    * @param timeLimit : max time to detect collision, in (0, 1] range.
    * @param p1Response : If collision is detected, update the collision time and response for p1.
    *                   Otherwise, set collision time to infinity.
    */
   public static void pointIntersectsPoint(
         float p1X, float p1Y, float p1SpeedX, float p1SpeedY, float p1Radius, 
         float p2X, float p2Y, float p2Radius,
         float timeLimit, CollisionResponse p1Response) {

      // Assumptions:
      assert (p1Radius >= 0) : "Negative radius!";
      assert (timeLimit > 0) : "Non-positive time";

      p1Response.reset();  // Set detected collision time to infinity
      
      // Call helper method to compute and return the collision time t.
      float t = pointIntersectsMovingPointDetection(
            p1X, p1Y, p1SpeedX, p1SpeedY, p1Radius, 
            p2X, p2Y, 0f, 0f, p2Radius);
      
      // Accept 0 < t <= timeLimit
      if (t > 0 && t <= timeLimit) {
         // Call helper method to compute and return the response given collision time t.
         pointIntersectsPointResponse(p1X, p1Y, p1SpeedX, p1SpeedY, p2X, p2Y, p1Response, t);
      }
   }
   
   /**
    * Helper method to compute the collision response given the collision time (t),
    * for a moving point hitting a stationary point.
    * Store and return the results in the given CollisionResponse object.
    * 
    * @param p1X : x-position of the center of point p1.
    * @param p1Y : y-position of the center of point p1.
    * @param p1SpeedX : p1's speed in x-direction.
    * @param p1SpeedY : p1's speed in y-direction.
    * @param p2X : x-position of the center of point p2.
    * @param p2Y : y-position of the center of point p2.
    * @param p1Response : To update the collision time and response for p1.
    *                     Reset time to infinity if error is detected.
    * @param t : the given detected collision time.
    */
   private static void pointIntersectsPointResponse(
         float p1X, float p1Y, float p1SpeedX, float p1SpeedY,
         float p2X, float p2Y,
         CollisionResponse p1Response, float t) {
      
      p1Response.t = t; // Update collision time in response
      
      // Need to get the point of impact to form the line of collision.
      double p1ImpactX = p1Response.getImpactX(p1X, p1SpeedX); 
      double p1ImpactY = p1Response.getImpactY(p1Y, p1SpeedY); 
      
      // Direction along the line of collision is P, normal is N.
      // Get the direction along the line of collision
      double lineAngle = Math.atan2(p2Y - p1ImpactY, p2X - p1ImpactX);

      // Project velocities from (x ,y) to (p, n)
      double[] result = rotate(p1SpeedX, p1SpeedY, lineAngle);
      double p1SpeedP = result[0];
      double p1SpeedN = result[1];

      // Collision possible only if p1SpeedP > 0
      // Needed to resolve overlaps.
      if (p1SpeedP <= 0) {
         //System.out.println("velocities cannot collide! t = " + t);
         p1Response.reset();  // No collision, keep moving.
         return;
      }

      double p1SpeedPAfter = -p1SpeedP;
      double p1SpeedNAfter = p1SpeedN;

      // Project the velocities back from (p, n) to (x, y)
      result = rotate(p1SpeedPAfter, p1SpeedNAfter, -lineAngle);
      p1Response.newSpeedX = (float)result[0];
      p1Response.newSpeedY = (float)result[1];
   }

   /**
    * Helper method to rotation vector (x, y) by theta, in Graphics coordinates.
    * y-axis is inverted. 
    * theta measured in counter-clockwise direction.
    * Re-use the double[] rotateResult to avoid repeated new operations.
    * 
    * @param x : x coordinate of the vector to be rotated.
    * @param y : y coordinate of the vector to be rotated, inverted.
    * @param theta : rotational angle in radians, counter-clockwise.
    * @return An double array of 2 elements x and y, in the rotated coordinates.
    */
   private static double[] rotateResult = new double[2];
   private static double[] rotate(double x, double y, double theta) {
      double sinTheta = Math.sin(theta);
      double cosTheta = Math.cos(theta);
      rotateResult[0] = x * cosTheta + y * sinTheta;
      rotateResult[1] = -x * sinTheta + y * cosTheta;
      return rotateResult;
   }

   /**
    * Helper method to get the absolute speed.
    * 
    * @param speedX : speed in x-direction.
    * @param speedY : speed in y-direction.
    * @return : magnitude of speed.
    */
   private static double getSpeed(double speedX, double speedY) {
      return Math.sqrt(speedX * speedX + speedY * speedY);
   }
}
