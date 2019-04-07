package Audio;


//import org.urish.openal.*;
//import org.urish.openal.jna.ALFactory;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;

public class SoundSample {
    /*static private OpenAL al = null;
    static {
        try {
            al = new OpenAL();
        } catch (ALException e) {
            al = null;
            e.printStackTrace();
        }
    }

    private Source src = null;

    public SoundSample(String wav) throws UnsupportedAudioFileException, ALException, IOException {
        try {
            src = al.createSource(new File(wav));
        } catch (UnsupportedAudioFileException e) {
            src = null;
            throw e;
        } catch (ALException e) {
            src = null;
            throw e;
        } catch (IOException e) {
            src = null;
            throw e;
        }

    }

    OutputStream createAudioStream() {
        //return src.createOutputStream(AudioFormat);
        return null;
    }*/
}