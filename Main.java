package ca.ubc.ece.cpen221.mp4;

import javax.swing.SwingUtilities;

import ca.ubc.ece.cpen221.mp4.ai.*;
import ca.ubc.ece.cpen221.mp4.chessPieces.Knight;
import ca.ubc.ece.cpen221.mp4.chessPieces.Pawn;
import ca.ubc.ece.cpen221.mp4.chessPieces.Queen;
import ca.ubc.ece.cpen221.mp4.items.Gardener;
import ca.ubc.ece.cpen221.mp4.items.Grass;
import ca.ubc.ece.cpen221.mp4.items.animals.*;
import ca.ubc.ece.cpen221.mp4.staff.WorldImpl;
import ca.ubc.ece.cpen221.mp4.staff.WorldUI;
import ca.ubc.ece.cpen221.mp4.vehicles.copy.Car;
import ca.ubc.ece.cpen221.mp4.vehicles.copy.Motorcycle;
import ca.ubc.ece.cpen221.mp4.vehicles.copy.Truck;

/**
 * The Main class initialize a world with some {@link Grass}, {@link Rabbit}s,
 * {@link Fox}es, {@link Gnat}s, {@link Gardener}, etc.
 *
 * You may modify or add Items/Actors to the World.
 *
 */
public class Main {

	static final int X_DIM = 40;
	static final int Y_DIM = 40;
	static final int SPACES_PER_GRASS = 7;
	static final int INITIAL_GRASS = X_DIM * Y_DIM / SPACES_PER_GRASS;
	static final int INITIAL_GNATS = INITIAL_GRASS / 4;
	static final int INITIAL_RABBITS = INITIAL_GRASS / 4;
	static final int INITIAL_FOXES = INITIAL_GRASS / 32;
	static final int INITIAL_TIGERS = INITIAL_GRASS / 32;
	static final int INITIAL_BEARS = INITIAL_GRASS / 40;
	static final int INITIAL_HYENAS = INITIAL_GRASS / 32;
	static final int INITIAL_CARS = INITIAL_GRASS / 50;
	static final int INITIAL_TRUCKS = INITIAL_GRASS / 50;
	static final int INITIAL_MOTORCYCLES = INITIAL_GRASS / 64;
	static final int INITIAL_KNIGHTS = 3;
	static final int INITIAL_PAWNS = 8;
	static final int INITIAL_QUEENS = 1;

	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				new Main().createAndShowWorld();
			}
		});
	}

	public void createAndShowWorld() {
		World world = new WorldImpl(X_DIM, Y_DIM);
		initialize(world);
		new WorldUI(world).show();
	}

	public void initialize(World world) {
		addGrass(world);
		world.addActor(new Gardener());

		addGnats(world);
		addRabbits(world);
		addFoxes(world);
		addMotorcycles(world);
		addCars(world);
		addTrucks(world);
		addBears(world);
		addHyenas(world);
		addTigers(world);
		addKnights(world);
		addPawns(world);
		addQueens(world);
	}

	private void addGrass(World world) {
		for (int i = 0; i < INITIAL_GRASS; i++) {
			Location loc = Util.getRandomEmptyLocation(world);
			world.addItem(new Grass(loc));
		}
	}

	private void addGnats(World world) {
		for (int i = 0; i < INITIAL_GNATS; i++) {
			Location loc = Util.getRandomEmptyLocation(world);
			Gnat gnat = new Gnat(loc);
			world.addItem(gnat);
			world.addActor(gnat);
		}
	}

	private void addFoxes(World world) {
		FoxAI foxAI = new FoxAI();
		for (int i = 0; i < INITIAL_FOXES; i++) {
			Location loc = Util.getRandomEmptyLocation(world);
			Fox fox = new Fox(foxAI, loc);
			world.addItem(fox);
			world.addActor(fox);
		}
	}

	private void addRabbits(World world) {
		RabbitAI rabbitAI = new RabbitAI();
		for (int i = 0; i < INITIAL_RABBITS; i++) {
			Location loc = Util.getRandomEmptyLocation(world);
			Rabbit rabbit = new Rabbit(rabbitAI, loc);
			world.addItem(rabbit);
			world.addActor(rabbit);
		}
	}

	private void addBears(World world) {
		for (int i = 0; i < INITIAL_BEARS; i++) {
			Location loc = Util.getRandomEmptyLocation(world);
			Bear bear = new Bear(loc);
			world.addItem(bear);
			world.addActor(bear);
		}
	}

	private void addHyenas(World world) {
		for (int i = 0; i < INITIAL_HYENAS; i++) {
			Location loc = Util.getRandomEmptyLocation(world);
			Hyena hyena = new Hyena(loc);
			world.addItem(hyena);
			world.addActor(hyena);
		}
	}

	private void addTigers(World world) {
		for (int i = 0; i < INITIAL_TIGERS; i++) {
			Location loc = Util.getRandomEmptyLocation(world);
			Tiger tiger = new Tiger(loc);
			world.addItem(tiger);
			world.addActor(tiger);
		}
	}

	private void addMotorcycles(World world) {
		VehicleAI vAI = new VehicleAI();

		for (int i = 0; i < INITIAL_MOTORCYCLES; i++) {
			Location loc = Util.getRandomEmptyLocation(world);
			Motorcycle cycle = new Motorcycle(vAI, loc);
			world.addItem(cycle);
			world.addActor(cycle);
		}
	}

	private void addKnights(World world) {
		KnightAI knightAI = new KnightAI();

		for (int i = 0; i < INITIAL_KNIGHTS; i++) {
			Location loc = Util.getRandomEmptyLocation(world);
			Knight knight = new Knight(knightAI, loc);
			world.addItem(knight);
			world.addActor(knight);
		}
	}

	private void addPawns(World world) {
		PawnAI pawnAI = new PawnAI();

		for (int i = 0; i < INITIAL_PAWNS; i++) {
			Location loc = Util.getRandomEmptyLocation(world);
			Pawn pawn = new Pawn(pawnAI, loc);
			world.addItem(pawn);
			world.addActor(pawn);
		}
	}

	private void addQueens(World world) {
		QueenAI queenAI = new QueenAI();

		for (int i = 0; i < INITIAL_QUEENS; i++) {
			Location loc = Util.getRandomEmptyLocation(world);
			Queen queen = new Queen(queenAI, loc);
			world.addItem(queen);
			world.addActor(queen);
		}
	}

	private void addCars(World world) {
		VehicleAI vAI = new VehicleAI();

		for (int i = 0; i < INITIAL_CARS; i++) {
			Location loc = Util.getRandomEmptyLocation(world);
			Car car = new Car(vAI, loc);
			world.addItem(car);
			world.addActor(car);
		}
	}

	private void addTrucks(World world) {
		VehicleAI vAI = new VehicleAI();

		for (int i = 0; i < INITIAL_TRUCKS; i++) {
			Location loc = Util.getRandomEmptyLocation(world);
			Truck truck = new Truck(vAI, loc);
			world.addItem(truck);
			world.addActor(truck);
		}
	}

}