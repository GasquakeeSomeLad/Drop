package breakfast.software.drop.client;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.backends.gwt.GwtApplication;
import com.badlogic.gdx.backends.gwt.GwtApplicationConfiguration;
import breakfast.software.drop.Drop;

public class HtmlLauncher extends GwtApplication {

        @Override
        public GwtApplicationConfiguration getConfig () {
        	return new GwtApplicationConfiguration(800, 480);
        }

        @Override
        public ApplicationListener getApplicationListener () {
        	return new Drop();
        }
}