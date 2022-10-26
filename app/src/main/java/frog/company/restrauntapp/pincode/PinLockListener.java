package frog.company.restrauntapp.pincode;


public interface PinLockListener {

    void onComplete(String pin);

    void onEmpty();

    void onPinChange(int pinLength, String intermediatePin);
}
