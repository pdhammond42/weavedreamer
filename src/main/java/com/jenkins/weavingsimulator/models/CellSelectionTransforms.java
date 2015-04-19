package com.jenkins.weavingsimulator.models;

import java.util.List;

/** A collection of factory methods for grid transforms
 * 
 * @author pete
 *
 */
public class CellSelectionTransforms {
	
	/** Factory method that creates a transform object that scales the
	 * selection in the vertical direction.
	 * @param scale Multiplier for number of rows
	 * @return A transform object
	 */
	public static CellSelectionTransform ScaleVertical (final int scale) {
		if (scale <= 0) throw new IllegalArgumentException();
		
		return new CellSelectionTransform () {
			public PasteGrid Transform(PasteGrid from) {
				PasteGrid s = new PasteGrid (from.getRows() * scale, from.getColumns());
				for (int r = 0; r != from.getRows(); ++r) {
					for (int c = 0; c != from.getColumns(); ++c) {
						for (int i = 0; i != scale; ++i) {
							s.setValue (r * scale + i, c, from.getValue(r,  c));							
						}
					}
				}
				return s;
			}
		};
	}
	
	/** Factory method that creates a transform object that scales the
	 * selection in the horizontal direction.
	 * @param scale Multiplier for number of columns
	 * @return A transform object
	 */
	public static CellSelectionTransform ScaleHorizontal (final int scale) {
		if (scale <= 0) throw new IllegalArgumentException();
		
		return new CellSelectionTransform () {
			public PasteGrid Transform(PasteGrid from) {
				PasteGrid s = new PasteGrid (from.getRows(), from.getColumns() * scale);
				for (int r = 0; r != from.getRows(); ++r) {
					for (int c = 0; c != from.getColumns(); ++c) {
						for (int i = 0; i != scale; ++i) {
							s.setValue (r, c * scale + i, from.getValue(r,  c));							
						}
					}
				}
				return s;
			}
		};
	}
	
	/** Factory method that creates a transform object that reflects the
	 * selection in the vertical axis
	 * @return A transform object
	 */
	public static CellSelectionTransform ReflectVertical () {
		return new CellSelectionTransform () {
			public PasteGrid Transform(PasteGrid from) {
				PasteGrid s = new PasteGrid (from.getRows(), from.getColumns());
				for (int r = 0; r != from.getRows(); ++r) {
					for (int c = 0; c != from.getColumns(); ++c) {
							s.setValue (r, c, from.getValue(r,  from.getColumns() - c - 1));							
					}
				}
				return s;
			}
		};
	}

	/** Factory method that creates a transform object that reflects the
	 * selection in the horizontal axis
	 * @return A transform object
	 */
	public static CellSelectionTransform ReflectHorizontal () {
		return new CellSelectionTransform () {
			public PasteGrid Transform(PasteGrid from) {
				PasteGrid s = new PasteGrid (from.getRows(), from.getColumns());
				for (int r = 0; r != from.getRows(); ++r) {
					for (int c = 0; c != from.getColumns(); ++c) {
							s.setValue (r, c, from.getValue(from.getRows() - r - 1,  c));							
					}
				}
				return s;
			}
		};
	}

	/** Factory method that creates a transform object that transposes
	 * the selection
	 * @return A transform object
	 */
	public static CellSelectionTransform Transpose () {
		return new CellSelectionTransform () {
			public PasteGrid Transform(PasteGrid from) {
				PasteGrid s = new PasteGrid (from.getColumns(), from.getRows());

				for (int r = 0; r != from.getRows(); ++r) {
					for (int c = 0; c != from.getColumns(); ++c) {
						s.setValue (c, r, from.getValue(from.getRows() - r - 1,  from.getColumns() - c - 1));							
					}
				}
				return s;
			}
		};
	};
	
	/** Factory method that creates a transform object that applies
	 * the given transforms in order.
	 * @return A transform object
	 */
	public static CellSelectionTransform Combine (final List<CellSelectionTransform> transforms) {
		return new CellSelectionTransform () {
			public PasteGrid Transform(PasteGrid from) {
				PasteGrid s = from;
				for (CellSelectionTransform t : transforms) {
					s = t.Transform(s);
				}
				return s;
			}
		};
	};
	
	/** Factory method that creates a Active Null object, which does not transform.
	 * 
	 * @return
	 */
	public static CellSelectionTransform Null () {
		return new CellSelectionTransform () {
			public PasteGrid Transform(PasteGrid from) {
				return from;
			}
		};
	}
}
