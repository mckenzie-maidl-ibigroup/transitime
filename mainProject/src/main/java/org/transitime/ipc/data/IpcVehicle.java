/*
 * This file is part of Transitime.org
 * 
 * Transitime.org is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License (GPL) as published by
 * the Free Software Foundation, either version 3 of the License, or
 * any later version.
 *
 * Transitime.org is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Transitime.org .  If not, see <http://www.gnu.org/licenses/>.
 */
package org.transitime.ipc.data;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InvalidObjectException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

import org.transitime.core.BlockAssignmentMethod;
import org.transitime.core.SpatialMatch;
import org.transitime.core.TemporalDifference;
import org.transitime.core.VehicleState;

import net.jcip.annotations.Immutable;

/**
 * Contains information on a single vehicle. For providing info to client. This
 * class is Immutable so that it is threadsafe.
 *
 * @author SkiBu Smith
 *
 */
@Immutable
public class IpcVehicle implements Serializable {

	private final String blockId;
	private final BlockAssignmentMethod blockAssignmentMethod;
	private final IpcAvl avl;
	private final float heading;
	private final String routeId;

	// routeShortName needed because routeId is sometimes not consistent over
	// schedule changes but routeShortName usually is.
	private final String routeShortName;
	private final String tripId;
	private final String directionId;
	private final String headsign;
	private final boolean predictable;
	private final TemporalDifference realTimeSchedAdh;
	private final boolean isLayover;
	private final String nextStopId;

	private static final long serialVersionUID = -1744566765456572042L;

	/********************** Member Functions **************************/

	/**
	 * Constructs a new Vehicle object from data in a VehicleState object.
	 * 
	 * @param vs
	 */
	public IpcVehicle(VehicleState vs) {
		this.blockId = vs.getBlock().getId();
		this.blockAssignmentMethod = vs.getAssignmentMethod();
		this.avl = new IpcAvl(vs.getAvlReport());
		this.heading = vs.getHeading();
		this.routeId = vs.getRouteId();
		this.routeShortName = vs.getRouteShortName();
		if (vs.getTrip() != null) {
			this.tripId = vs.getTrip().getId();
			this.directionId = vs.getTrip().getDirectionId();
			this.headsign = vs.getTrip().getHeadsign();
			
			// Get the match. If match is just after a stop then adjust
			// it to just before the stop so that can determine proper 
			// stop ID and such.
			SpatialMatch match = vs.getMatch().getMatchBeforeStopIfAtStop();

			this.isLayover = match.isLayover();

			// If vehicle is at a stop then "next" stop will actually be
			// the current stop.
			this.nextStopId = match.getStopPath().getStopId();

		} else {
			// Vehicle not assigned to trip so null out parameters
			this.tripId = null;
			this.directionId = null;
			this.headsign = null;
			this.isLayover = false;
			this.nextStopId = null;
		}
		this.predictable = vs.isPredictable();
		this.realTimeSchedAdh = vs.getRealTimeSchedAdh();
	}

	/**
	 * Constructor used for when deserializing a proxy object. Declared
	 * protected because only used internally by the proxy class but also for
	 * sub class.
	 * 
	 * @param blockId
	 * @param blockAssignmentMethod
	 * @param avl
	 * @param pathHeading
	 * @param routeId
	 * @param routeShortName
	 * @param tripId
	 * @param directionId
	 * @param headsign
	 * @param predictable
	 * @param realTimeSchdAdh
	 * @param isLayover
	 * @param nextStopId
	 */
	protected IpcVehicle(String blockId,
			BlockAssignmentMethod blockAssignmentMethod, IpcAvl avl,
			float heading, String routeId, String routeShortName,
			String tripId, String directionId, String headsign,
			boolean predictable, TemporalDifference realTimeSchdAdh,
			boolean isLayover, String nextStopId) {
		this.blockId = blockId;
		this.blockAssignmentMethod = blockAssignmentMethod;
		this.avl = avl;
		this.heading = heading;
		this.routeId = routeId;
		this.routeShortName = routeShortName;
		this.tripId = tripId;
		this.directionId = directionId;
		this.headsign = headsign;
		this.predictable = predictable;
		this.realTimeSchedAdh = realTimeSchdAdh;
		this.isLayover = isLayover;
		this.nextStopId = nextStopId;
	}

	/*
	 * SerializationProxy is used so that this class can be immutable and so
	 * that can do versioning of objects.
	 */
	protected static class SerializationProxy implements Serializable {
		// Exact copy of fields of IpcVehicle enclosing class object
		protected String blockId;
		protected BlockAssignmentMethod blockAssignmentMethod;
		protected IpcAvl avl;
		protected float heading;
		protected String routeId;
		protected String routeShortName;
		protected String tripId;
		protected String directionId;
		protected String headsign;
		protected boolean predictable;
		protected TemporalDifference realTimeSchdAdh;
		protected boolean isLayover;
		protected String nextStopId;

		private static final long serialVersionUID = -4996254752417270043L;
		private static final short serializationVersion = 0;

		/*
		 * Only to be used within this class.
		 */
		protected SerializationProxy(IpcVehicle v) {
			this.blockId = v.blockId;
			this.blockAssignmentMethod = v.blockAssignmentMethod;
			this.avl = v.avl;
			this.heading = v.heading;
			this.routeId = v.routeId;
			this.routeShortName = v.routeShortName;
			this.tripId = v.tripId;
			this.directionId = v.directionId;
			this.headsign = v.headsign;
			this.predictable = v.predictable;
			this.realTimeSchdAdh = v.realTimeSchedAdh;
			this.isLayover = v.isLayover;
			this.nextStopId = v.nextStopId;
		}

		/*
		 * When object is serialized writeReplace() causes this
		 * SerializationProxy object to be written. Write it in a custom way
		 * that includes a version ID so that clients and servers can have two
		 * different versions of code.
		 */
		protected void writeObject(java.io.ObjectOutputStream stream)
				throws IOException {
		    stream.writeShort(serializationVersion);
		    
			stream.writeObject(blockId);
			stream.writeObject(blockAssignmentMethod);
			stream.writeObject(avl);
			stream.writeFloat(heading);
			stream.writeObject(routeId);
			stream.writeObject(routeShortName);
			stream.writeObject(tripId);
			stream.writeObject(directionId);
		    stream.writeObject(headsign);
			stream.writeBoolean(predictable);
			stream.writeObject(realTimeSchdAdh);
		    stream.writeBoolean(isLayover);
		    stream.writeObject(nextStopId);
		}

		/*
		 * Custom method of deserializing a SerializationProy object.
		 */
		protected void readObject(java.io.ObjectInputStream stream)
				throws IOException, ClassNotFoundException {
			short readVersion = stream.readShort();
			if (serializationVersion != readVersion) {
				throw new IOException("Serialization error when reading "
						+ getClass().getSimpleName()
						+ " object. Read serializationVersion=" + readVersion);
			}

			// serialization version is OK so read in object
			blockId = (String) stream.readObject();
			blockAssignmentMethod = (BlockAssignmentMethod) stream.readObject();
			avl = (IpcAvl) stream.readObject();
			heading = stream.readFloat();
			routeId = (String) stream.readObject();
			routeShortName = (String) stream.readObject();
			tripId = (String) stream.readObject();
			directionId = (String) stream.readObject();
			headsign = (String) stream.readObject();
			predictable = stream.readBoolean();
			realTimeSchdAdh = (TemporalDifference) stream.readObject();
			isLayover = stream.readBoolean();
			nextStopId = (String) stream.readObject();
		}

		/*
		 * When an object is read in it will be a SerializatProxy object due to
		 * writeReplace() being used by the enclosing class. When such an object
		 * is deserialized this method will be called and the SerializationProxy
		 * object is converted to an enclosing class object.
		 */
		private Object readResolve() {
			return new IpcVehicle(blockId, blockAssignmentMethod, avl,
					heading, routeId, routeShortName, tripId, directionId,
					headsign, predictable, realTimeSchdAdh, isLayover,
					nextStopId);
		}
	} // End of SerializationProxy class

	/*
	 * Needed as part of using a SerializationProxy. When IpcVehicle object is
	 * serialized the SerializationProxy will instead be used.
	 */
	private Object writeReplace() {
		return new SerializationProxy(this);
	}

	/*
	 * Needed as part of using a SerializationProxy. Makes sure that Vehicle
	 * object cannot be deserialized without using proxy, thereby eliminating
	 * possibility of such an attack as described in "Effective Java".
	 */
	private void readObject(ObjectInputStream stream)
			throws InvalidObjectException {
		throw new InvalidObjectException("Must use proxy instead");
	}

	public String getId() {
		return avl.getVehicleId();
	}

	public String getBlockId() {
		return blockId;
	}

	public BlockAssignmentMethod getBlockAssignmentMethod() {
		return blockAssignmentMethod;
	}

	public IpcAvl getAvl() {
		return avl;
	}

	/**
	 * Returns number of degrees clockwise from due North. Note that this is
	 * very different from angle(). If GPS heading not available (either
	 * because AVL system doesn't provide it or speed is too low such that
	 * the heading is not currently valid) then the path heading where the
	 * vehicle matches is used.
	 * 
	 * @return Heading of vehicle, or null if speed not defined.
	 */
	public float getHeading() {
		return heading;
	}

	/**
	 * @return Speed of vehicle, or null if speed not defined.
	 */
	public float getSpeed() {
		return avl.getSpeed();
	}

	public float getLatitude() {
		return avl.getLatitude();
	}

	public float getLongitude() {
		return avl.getLongitude();
	}

	public String getLicensePlate() {
		return avl.getLicensePlate();
	}

	public long getGpsTime() {
		return avl.getTime();
	}

	public String getRouteId() {
		return routeId;
	}

	public String getRouteShortName() {
		return routeShortName;
	}

	public String getTripId() {
		return tripId;
	}

	public String getDirectionId() {
		return directionId;
	}
	
	public String getHeadsign() {
		return headsign;
	}

	public boolean isPredictable() {
		return predictable;
	}

	public TemporalDifference getRealTimeSchedAdh() {
		return realTimeSchedAdh;
	}

	public boolean isLayover() {
		return isLayover;
	}

	public String getNextStopId() {
		return nextStopId;
	}

	@Override
	public String toString() {
		return "IpcVehicle [" 
				+ "vehicleId=" + avl.getVehicleId() 
				+ ", blockId=" + blockId 
				+ ", blockAssignmentMethod=" + blockAssignmentMethod
				+ ", routeId=" + routeId 
				+ ", routeShortName=" + routeShortName
				+ ", tripId=" + tripId 
				+ ", directionId=" + directionId
				+ ", headsign=" + headsign
				+ ", predictable=" + predictable
				+ ", realTimeSchedAdh=" + realTimeSchedAdh 
				+ ", avl=" + avl
				+ ", heading=" + heading 
				+ ", isLayover=" + isLayover
				+ ", nextStopId=" + nextStopId 
				+ "]";
	}

	/*
	 * Just for testing.
	 */
	public static void main(String args[]) {
		IpcAvl avl = new IpcAvl("avlVehicleId", 10, 1.23f, 4.56f, 0.0f, 0.0f,
				"block", "driver", "license", 0);
		IpcVehicle v = new IpcVehicle("blockId",
				BlockAssignmentMethod.AVL_FEED_BLOCK_ASSIGNMENT, avl, 123.456f,
				"routeId", "routeShortName", "tripId", "dirId", "headsign",
				true, null, false, null);
		try {
			FileOutputStream fileOut = new FileOutputStream("foo.ser");
			ObjectOutputStream outStream = new ObjectOutputStream(fileOut);
			outStream.writeObject(v);
			outStream.close();
			fileOut.close();
		} catch (IOException i) {
			i.printStackTrace();
		}

		try {
			FileInputStream fileIn = new FileInputStream("foo.ser");
			ObjectInputStream in = new ObjectInputStream(fileIn);
			@SuppressWarnings("unused")
			IpcVehicle newVehicle = (IpcVehicle) in.readObject();
			in.close();
			fileIn.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

}