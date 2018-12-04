package com.jenkins.weavedreamer.models;

import java.util.ArrayList;

import junit.framework.TestCase;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class CellSelectionTransformsTest  extends TestCase {
	private PasteGrid selection;
	public void setUp() {
		selection = new PasteGrid (3, 2);
		// Setup up a selection for transforming:
		// *.
		// *.
		// .*
		selection.setValue(0, 0, true);
		selection.setValue(0, 1, false);
		selection.setValue(1, 0, true);
		selection.setValue(1, 1, false);
		selection.setValue(2, 0, false);
		selection.setValue(2, 1, true);
	}
	
	public void testSelectionToString () {
		assertThat (selection.toString(), is (
				"*.;" + 
				"*.;" +	
				".*;"));
	}
	
	public void testVerticalScale () {
		PasteGrid cells = 
				CellSelectionTransforms.ScaleVertical (3).Transform(selection);
		assertThat (cells.toString(), is (
				"*.;" + 
				"*.;" +	
				"*.;" +	
				"*.;" +	
				"*.;" +	
				"*.;" +	
				".*;" +	
				".*;" +	
				".*;"));
	}
	
	public void testVerticalScaleMustBePositive () {
		try {
			CellSelectionTransforms.ScaleVertical (-3).Transform(selection);
			fail("Expected Illegal Argument Exception");
		}
		catch(IllegalArgumentException e) {
		
		}
	}
	
	public void testHorizontalScale () {
		PasteGrid cells = 
				CellSelectionTransforms.ScaleHorizontal (3).Transform(selection);
		assertThat (cells.toString(), is (
				"***...;" + 
				"***...;" +	
				"...***;" ));
	}
	
	public void testHorzontalScaleMustBePositive () {
		try {
			CellSelectionTransforms.ScaleHorizontal (-3).Transform(selection);
			fail("Expected Illegal Argument Exception");
		}
		catch(IllegalArgumentException e) {
		
		}
	}

	public void testVerticalReflect () {
		PasteGrid cells = 
				CellSelectionTransforms.ReflectVertical().Transform(selection);
		assertThat (cells.toString(), is (
				".*;" + 
				".*;" +	
				"*.;" ));
	}	

	public void testHorizontalReflect () {
		PasteGrid cells = 
				CellSelectionTransforms.ReflectHorizontal().Transform(selection);
		assertThat (cells.toString(), is (
				".*;" + 
				"*.;" +	
				"*.;" ));
	}	
	
	public void testTranspose () {
		PasteGrid cells = 
				CellSelectionTransforms.Transpose().Transform(selection);
		assertThat (cells.toString(), is (
				"*..;" + 
				".**;"));
	}

	public void testRepeatHorizontal() {
		PasteGrid cells =
				CellSelectionTransforms.RepeatHorizontal(2).Transform(selection);
		assertThat (cells.toString(), is (
				"*.*.;" +
						"*.*.;" +
						".*.*;" ));

	}

	public void testRepeatVertical() {
		PasteGrid cells =
				CellSelectionTransforms.RepeatVertical(2).Transform(selection);
		assertThat (cells.toString(), is (
				"*.;" +
						"*.;" +
						".*;" +
						"*.;" +
						"*.;" +
						".*;"));

	}

	public void testCompositeTransform() {
		ArrayList<CellSelectionTransform> transforms = new ArrayList<CellSelectionTransform>();
		transforms.add(CellSelectionTransforms.ReflectHorizontal());
		transforms.add(CellSelectionTransforms.ScaleHorizontal(2));
		
		PasteGrid cells = 
				CellSelectionTransforms.Combine(transforms).Transform(selection);
		assertThat (cells.toString(), is (
				"..**;" + 
				"**..;" +		
				"**..;" ));
	}
}
