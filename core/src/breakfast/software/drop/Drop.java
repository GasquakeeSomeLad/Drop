package breakfast.software.drop;
//http://dist.springsource.com/release/TOOLS/gradle

import java.util.Iterator;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.TimeUtils;

public class Drop implements ApplicationListener {
	private long lastDropTime;
	
	private OrthographicCamera camera;
	private SpriteBatch batch;
	
	private Rectangle bucket;
	private Array<Rectangle> drops;
	
	private Texture dropletImage, bucketImage, background;
	private Sound drop;
	private Music rain;
	
	@Override
	public void create () {
		camera = new OrthographicCamera();
		camera.setToOrtho(false, 800, 480);
		
		batch = new SpriteBatch();
		
		bucket = new Rectangle();
		bucket.x = 368;
		bucket.y = 20;
		bucket.width = bucket.height = 64;
		
		drops = new Array<Rectangle>();
		spawnRaindrop();
		
		dropletImage = new Texture(Gdx.files.internal("droplet.png"));
		bucketImage = new Texture(Gdx.files.internal("bucket.png"));
		background = new Texture(Gdx.files.internal("clouds.png"));
		
		drop = Gdx.audio.newSound(Gdx.files.internal("drop.wav"));
		rain = Gdx.audio.newMusic(Gdx.files.internal("rain.mp3"));
		
		rain.setLooping(true);
		rain.play();
	}

	@Override
	public void render () {
		Gdx.gl.glClearColor(0, 0, 0.3f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		camera.update();
		
		batch.setProjectionMatrix(camera.combined);
		batch.begin();
		batch.draw(background, 0, 0);
		for(Rectangle raindrop: drops) {
			batch.draw(dropletImage, raindrop.x, raindrop.y);
		}
		batch.draw(bucketImage, bucket.x, bucket.y);
		batch.end();
		
		if(Gdx.input.isTouched()) {
		      Vector3 touchPos = new Vector3();
		      touchPos.set(Gdx.input.getX(), Gdx.input.getY(), 0);
		      camera.unproject(touchPos);
		      bucket.x = touchPos.x - 64 / 2;
		}
		
		 if(Gdx.input.isKeyPressed(Keys.LEFT))
			 bucket.x -= 250 * Gdx.graphics.getDeltaTime();
		 if(Gdx.input.isKeyPressed(Keys.RIGHT))
			 bucket.x += 250 * Gdx.graphics.getDeltaTime();
		 
		 if(bucket.x < 0)
			 bucket.x = 0;
		 if(bucket.x > 800 - bucket.width)
			 bucket.x = 800 - bucket.width;
		 
		 if(TimeUtils.nanoTime() - lastDropTime > 1000000000)
			 spawnRaindrop();
		 
		   Iterator<Rectangle> iter = drops.iterator();
		   while(iter.hasNext()) {
		      Rectangle raindrop = iter.next();
		      raindrop.y -= 200 * Gdx.graphics.getDeltaTime();
		      if(raindrop.y + 64 < 0)
		    	  iter.remove();
		      else if(raindrop.overlaps(bucket)) {
		          drop.play();
		          iter.remove();
		       }
		   }
	}
	
	private void spawnRaindrop() {
		Rectangle raindrop = new Rectangle();
		raindrop.x = MathUtils.random(0, 800-64);
		raindrop.y = 480;
		raindrop.width = 64;
		raindrop.height = 64;
		drops.add(raindrop);
		lastDropTime = TimeUtils.nanoTime();
	}

	@Override
	public void resize(int width, int height) {
		// TODO Auto-generated method stub
	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub
	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub
	}

	@Override
	public void dispose() {
	      dropletImage.dispose();
	      bucketImage.dispose();
	      background.dispose();
	      drop.dispose();
	      rain.dispose();
	      batch.dispose();
	}
}
