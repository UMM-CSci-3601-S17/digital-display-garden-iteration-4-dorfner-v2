package umm3601.plant;

public class GardenLocation {
    public String _id;

    @Override
    public boolean equals(Object that) {
        if (!(that instanceof GardenLocation)) {
            return false;
        }
        return ((GardenLocation) that)._id.equals(this._id);
    }
}