package ca.ubc.ece.cpen221.mp4.ai;

import java.util.HashSet;
import java.util.Set;

import ca.ubc.ece.cpen221.mp4.Direction;
import ca.ubc.ece.cpen221.mp4.Location;
import ca.ubc.ece.cpen221.mp4.Util;
import ca.ubc.ece.cpen221.mp4.commands.Command;
import ca.ubc.ece.cpen221.mp4.commands.EatCommand;
import ca.ubc.ece.cpen221.mp4.commands.MoveCommand;
import ca.ubc.ece.cpen221.mp4.items.Item;

/**
 * Your Fox AI.
 */
public class FoxAI extends PredatorAI {

	public FoxAI() { 
		super();
	}
	
	@Override
	public Command eatCheck(){
		for (Item i : surrounding){
			if (animal.getLocation().getDistance(i.getLocation()) == 1 && i.getName().equals("Rabbit") &&
					animal.getEnergy() < animal.getMaxEnergy() - i.getMeatCalories()){
				return new EatCommand(animal, i);
			}
			if (animal.getLocation().getDistance(i.getLocation()) == 1 && i.getName().equals("Rabbit") &&
					animal.getEnergy() > animal.getMaxEnergy() - i.getMeatCalories()){
				return breedCheck(true);
			} 
			
		}
		return null;
	}
	
	@Override 
	public Command chaseCheck( AdjacencyListGraph graph) {
			
			//find best meal
			Set<Item> edible = new HashSet<Item>();
			for (Item i : surrounding){
				if (i.getName().equals("Rabbit")){
					edible.add(i);
				}
			}
			while (!edible.isEmpty()){
				Item meal = null;
				boolean flag = true;
				
				for (Item i : edible){
					if (flag || i.getLocation().getDistance(this.animal.getLocation()) < meal.getLocation().getDistance(this.animal.getLocation())){
						meal = i;
						flag = false;
					}
				}
				/*
				Command shortDis = shortestDistance(meal.getLocation(), graph);
				if (shortDis != null){
					return shortDis;
				}
			
					*/	
				Direction dir = Util.getDirectionTowards(animal.getLocation(), meal.getLocation());
				
				Location targetLocation = new Location(animal.getLocation(),dir);
				
				if (Util.isValidLocation(world, targetLocation) && this.isLocationEmpty(world, animal, targetLocation)){
					return new MoveCommand(animal,targetLocation);
				}
				edible.remove(meal); 
			}
			
			return null;
		}

	/*
	@Override
	public Command coordinate() {
		for (Item fox : surrounding) {
			if (fox.getName().equals("Fox")) {
				for (Item rabbit : surrounding) {
					if (rabbit.getName().equals("Rabbit") && rabbit.getLocation().getDistance(animal.getLocation()) > 3 && 
							rabbit.getLocation().getDistance(fox.getLocation()) > 3 && Util.getDirectionTowards(animal.getLocation(), rabbit.getLocation())
									!= Util.getDirectionTowards(animal.getLocation(), fox.getLocation())) {
						Direction dir = Util.getDirectionTowards(fox.getLocation(), animal.getLocation());
						
						Location targetLocation = new Location(animal.getLocation(), dir);
						
						if (Util.isValidLocation(world, targetLocation) && this.isLocationEmpty(world, animal, targetLocation)){
							return new MoveCommand(animal,targetLocation);
						}
					}
					if (rabbit.getName().equals("Rabbit") && rabbit.getLocation().getDistance(animal.getLocation()) > 3 && 
							rabbit.getLocation().getDistance(fox.getLocation()) > 3 && Util.getDirectionTowards(animal.getLocation(), rabbit.getLocation())
									== Util.getDirectionTowards(animal.getLocation(), fox.getLocation())) {
						Direction dir = Util.getDirectionTowards(animal.getLocation(), fox.getLocation());
						
						Location targetLocation = new Location(animal.getLocation(), dir);
						
						if (Util.isValidLocation(world, targetLocation) && this.isLocationEmpty(world, animal, targetLocation)){
							return new MoveCommand(animal,targetLocation);
						}
					}
				}
			}
		}
		return null;
	}*/
}
