/*
 * Copyright (c) 2014, 2018 CEA and others.
 * 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v2.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v20.html
 *
 * Contributors:
 *   Christian W. Damus (CEA) - initial API and implementation
 *   Kenn Hussey - 535301
 *
 */
package org.eclipse.uml2.examples.gettingstarted;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.uml2.common.util.UML2Util;
import org.eclipse.uml2.uml.AggregationKind;
import org.eclipse.uml2.uml.Artifact;
import org.eclipse.uml2.uml.Association;
import org.eclipse.uml2.uml.Classifier;
import org.eclipse.uml2.uml.Comment;
import org.eclipse.uml2.uml.Component;
import org.eclipse.uml2.uml.Constraint;
import org.eclipse.uml2.uml.Device;
import org.eclipse.uml2.uml.Enumeration;
import org.eclipse.uml2.uml.EnumerationLiteral;
import org.eclipse.uml2.uml.ExecutionEnvironment;
import org.eclipse.uml2.uml.Generalization;
import org.eclipse.uml2.uml.Interface;
import org.eclipse.uml2.uml.LiteralUnlimitedNatural;
import org.eclipse.uml2.uml.Model;
import org.eclipse.uml2.uml.NamedElement;
import org.eclipse.uml2.uml.Node;
import org.eclipse.uml2.uml.PrimitiveType;
import org.eclipse.uml2.uml.Property;
import org.eclipse.uml2.uml.Relationship;
import org.eclipse.uml2.uml.Type;
import org.eclipse.uml2.uml.UMLFactory;
import org.eclipse.uml2.uml.UMLPackage;
import org.eclipse.uml2.uml.resource.UMLResource;
import org.eclipse.uml2.uml.resources.util.UMLResourcesUtil;

/**
 * A Java program that may be run stand-alone (with the required EMF and UML2
 * bundle JARs on the classpath) to create the example model illustrated in the
 * <em>Getting Started with UML2</em> article on the Wiki.
 * 
 * @see http://wiki.eclipse.org/MDT/UML2/Getting_Started_with_UML2
 */
public class GettingStartedWithUML2 {

	public static boolean DEBUG = true;

	private static File outputDir;
	private static int  nodes_num;

	/**
	 * The main program. It expects one argument, which is the local filesystem
	 * path of a directory in which to create the <tt>ExtendedPO2.uml</tt> file.
	 * 
	 * @param the
	 *            program arguments, which must consist of a single filesystem
	 *            path
	 */
	public static void main(String[] args)
			throws Exception {

		if (!processArgs(args)) {
			System.exit(1);
		}

		banner("Creating root model package and primitive types.");

		// Create the root package (a model).
		Model UML2Model = createModel("uml2");

		// Create primitive types to be used as types of attributes in our
		// classes.
		PrimitiveType intPrimitiveType = createPrimitiveType(UML2Model, "int");
		PrimitiveType stringPrimitiveType = createPrimitiveType(UML2Model,
			"String");
		PrimitiveType datePrimitiveType = createPrimitiveType(UML2Model, "Date");
		PrimitiveType skuPrimitiveType = createPrimitiveType(UML2Model, "SKU");

		// Create enumerations to be used as types of attributes in our classes.
		Enumeration orderStatusEnumeration = createEnumeration(UML2Model,
			"OrderStatus");
		createEnumerationLiteral(orderStatusEnumeration, "Pending");
		createEnumerationLiteral(orderStatusEnumeration, "BackOrder");
		createEnumerationLiteral(orderStatusEnumeration, "Complete");
		
		int i, avg = nodes_num/4;
		int num_last = nodes_num-3*avg;
		
		banner("Creating Package Diagram.");
		org.eclipse.uml2.uml.Package PackageDiagram = createPackage(UML2Model, "package diagram");
		generate_package_diagram(avg, PackageDiagram);
		
		banner("Creating Deployment Diagram.");
		org.eclipse.uml2.uml.Package DeploymentDiagram = createPackage(UML2Model, "deployment diagram");
		generate_deployment_diagram(avg, DeploymentDiagram);
		
		banner("Creating Component Diagram.");
		org.eclipse.uml2.uml.Package ComponentDiagram = createPackage(UML2Model, "component diagram");
		generate_component_diagram(avg, ComponentDiagram);
		
		/*
		banner("Creating Composite structure Diagram.");
		Model CompositeStructureDiagram = createModel("component diagram");
		*/
		
		/*
		banner("Creating model classes.");

		// Create the classes.
		org.eclipse.uml2.uml.Class supplierClass = createClass(epo2Model,
			"Supplier", false);
		org.eclipse.uml2.uml.Class customerClass = createClass(epo2Model,
			"Customer", false);
		org.eclipse.uml2.uml.Class purchaseOrderClass = createClass(epo2Model,
			"PurchaseOrder", false);
		org.eclipse.uml2.uml.Class itemClass = createClass(epo2Model, "Item",
			false);
		org.eclipse.uml2.uml.Class addressClass = createClass(epo2Model,
			"Address", true);
		org.eclipse.uml2.uml.Class usAddressClass = createClass(epo2Model,
			"USAddress", false);
		org.eclipse.uml2.uml.Class globalAddressClass = createClass(epo2Model,
			"GlobalAddress", false);
		org.eclipse.uml2.uml.Class globalLocationClass = createClass(epo2Model,
			"GlobalLocation", false);

		// Create generalization relationships amongst our classes.
		createGeneralization(usAddressClass, addressClass);
		createGeneralization(globalAddressClass, addressClass);
		createGeneralization(globalAddressClass, globalLocationClass);

		banner("Creating attributes of model classes.");

		// Create attributes in our classes.
		createAttribute(supplierClass, "name", stringPrimitiveType, 0, 1);
		createAttribute(customerClass, "customerID", intPrimitiveType, 1, 1);
		createAttribute(purchaseOrderClass, "comment", stringPrimitiveType, 0,
			1);
		createAttribute(purchaseOrderClass, "orderDate", datePrimitiveType, 1,
			1);
		createAttribute(purchaseOrderClass, "status", orderStatusEnumeration,
			1, 1);
		createAttribute(purchaseOrderClass, "totalAmount", intPrimitiveType, 0,
			1);
		createAttribute(itemClass, "productName", stringPrimitiveType, 0, 1);
		createAttribute(itemClass, "quantity", intPrimitiveType, 0, 1);
		createAttribute(itemClass, "usPrice", intPrimitiveType, 0, 1);
		createAttribute(itemClass, "comment", stringPrimitiveType, 0, 1);
		createAttribute(itemClass, "shipDate", datePrimitiveType, 0, 1);
		createAttribute(itemClass, "partNum", skuPrimitiveType, 1, 1);
		createAttribute(addressClass, "name", stringPrimitiveType, 0, 1);
		createAttribute(addressClass, "country", stringPrimitiveType, 0, 1);
		createAttribute(usAddressClass, "street", stringPrimitiveType, 1, 1);
		createAttribute(usAddressClass, "city", stringPrimitiveType, 1, 1);
		createAttribute(usAddressClass, "state", stringPrimitiveType, 1, 1);
		createAttribute(usAddressClass, "zip", stringPrimitiveType, 1, 1);
		createAttribute(globalAddressClass, "location", stringPrimitiveType, 1,
			1);
		createAttribute(globalLocationClass, "countryCode", intPrimitiveType,
			1, 1);
*/
		/*
		banner("Creating associations between model classes.");

		// Create associations between our classes.
		createAssociation(supplierClass, true,
			AggregationKind.COMPOSITE_LITERAL, "orders", 0,
			LiteralUnlimitedNatural.UNLIMITED, purchaseOrderClass, false,
			AggregationKind.NONE_LITERAL, "", 1, 1);
		createAssociation(supplierClass, true, AggregationKind.NONE_LITERAL,
			"pendingOrders", 0, LiteralUnlimitedNatural.UNLIMITED,
			purchaseOrderClass, false, AggregationKind.NONE_LITERAL, "", 0, 1);
		createAssociation(supplierClass, true, AggregationKind.NONE_LITERAL,
			"shippedOrders", 0, LiteralUnlimitedNatural.UNLIMITED,
			purchaseOrderClass, false, AggregationKind.NONE_LITERAL, "", 0, 1);
		createAssociation(supplierClass, true,
			AggregationKind.COMPOSITE_LITERAL, "customers", 0,
			LiteralUnlimitedNatural.UNLIMITED, customerClass, false,
			AggregationKind.NONE_LITERAL, "", 1, 1);
		createAssociation(customerClass, true, AggregationKind.NONE_LITERAL,
			"orders", 0, LiteralUnlimitedNatural.UNLIMITED, purchaseOrderClass,
			true, AggregationKind.NONE_LITERAL, "customer", 1, 1);
		createAssociation(purchaseOrderClass, true,
			AggregationKind.NONE_LITERAL, "previousOrder", 0, 1,
			purchaseOrderClass, false, AggregationKind.NONE_LITERAL, "", 0, 1);
		createAssociation(purchaseOrderClass, true,
			AggregationKind.COMPOSITE_LITERAL, "items", 0,
			LiteralUnlimitedNatural.UNLIMITED, itemClass, true,
			AggregationKind.NONE_LITERAL, "order", 1, 1);
		createAssociation(purchaseOrderClass, true,
			AggregationKind.COMPOSITE_LITERAL, "billTo", 1, 1, addressClass,
			false, AggregationKind.NONE_LITERAL, "", 1, 1);
		createAssociation(purchaseOrderClass, true,
			AggregationKind.COMPOSITE_LITERAL, "shipTo", 1, 1, addressClass,
			false, AggregationKind.NONE_LITERAL, "", 1, 1);
		
		*/

		// Save our model to a file in the user-specified output directory
		
		URI outputURI = URI.createFileURI(outputDir.getAbsolutePath())
				.appendSegment("UML models")
				.appendFileExtension(UMLResource.FILE_EXTENSION);
		
		banner("Saving the overall UML models to %s.", outputURI.toFileString());
		save(UML2Model, outputURI);
		/*
		URI outputURI_Package = URI.createFileURI(outputDir.getAbsolutePath())
			.appendSegment("package diagram")
			.appendFileExtension(UMLResource.FILE_EXTENSION);
		
		URI outputURI_Deployment = URI.createFileURI(outputDir.getAbsolutePath())
				.appendSegment("deployment diagram")
				.appendFileExtension(UMLResource.FILE_EXTENSION);
		URI outputURI_Component = URI.createFileURI(outputDir.getAbsolutePath())
				.appendSegment("component diagram")
				.appendFileExtension(UMLResource.FILE_EXTENSION);
		URI outputURI_CompositeStructure = URI.createFileURI(outputDir.getAbsolutePath())
				.appendSegment("composite structure diagram")
				.appendFileExtension(UMLResource.FILE_EXTENSION);
		//save(epo2Model, outputURI);
		
		banner("Saving the package diagram to %s.", outputURI_Package.toFileString());
		save(PackageDiagram, outputURI_Package);
		banner("Saving the deployment diagram to %s.", outputURI_Deployment.toFileString());
		save(DeploymentDiagram, outputURI_Deployment);
		banner("Saving the component diagram to %s.", outputURI_Component.toFileString());
		save(ComponentDiagram, outputURI_Component);
		banner("Saving the composite structure diagram to %s.", outputURI_CompositeStructure.toFileString());
		save(CompositeStructureDiagram, outputURI_CompositeStructure);
		*/
	}

	//
	// Model-building utilities
	//

	protected static Model createModel(String name) {
		Model model = UMLFactory.eINSTANCE.createModel();
		model.setName(name);

		out("Model '%s' created.", model.getQualifiedName());

		return model;
	}

	protected static org.eclipse.uml2.uml.Package createPackage(
			org.eclipse.uml2.uml.Package nestingPackage, String name) {

		org.eclipse.uml2.uml.Package package_ = nestingPackage
			.createNestedPackage(name);
		out("Package '%s' created.", package_.getQualifiedName());
		return package_;
	}

	protected static PrimitiveType createPrimitiveType(
			org.eclipse.uml2.uml.Package package_, String name) {

		PrimitiveType primitiveType = package_.createOwnedPrimitiveType(name);

		out("Primitive type '%s' created.", primitiveType.getQualifiedName());

		return primitiveType;
	}

	protected static Enumeration createEnumeration(
			org.eclipse.uml2.uml.Package package_, String name) {

		Enumeration enumeration = package_.createOwnedEnumeration(name);

		out("Enumeration '%s' created.", enumeration.getQualifiedName());

		return enumeration;
	}

	protected static EnumerationLiteral createEnumerationLiteral(
			Enumeration enumeration, String name) {

		EnumerationLiteral enumerationLiteral = enumeration
			.createOwnedLiteral(name);

		out("Enumeration literal '%s' created.",
			enumerationLiteral.getQualifiedName());

		return enumerationLiteral;
	}
/*
 * Methods for creating nodes in diagrams
 * 
 * */
	
	
	protected static org.eclipse.uml2.uml.PackageableElement create_model(org.eclipse.uml2.uml.Package package_, String name) {

		EClass eClass = UMLPackage.Literals.MODEL;
		org.eclipse.uml2.uml.PackageableElement model_ = package_.createPackagedElement(name, eClass);

		out("Model %s created.", name);
		
		return model_;
	}
	
	protected static org.eclipse.uml2.uml.PackageableElement create_package(org.eclipse.uml2.uml.Package package_, String name) {

		EClass eClass = UMLPackage.Literals.PACKAGE;
		org.eclipse.uml2.uml.PackageableElement package_in_diagram = package_.createPackagedElement(name, eClass);

		out("Package %s created.", name);
		
		return package_in_diagram;
	}
	
	protected static org.eclipse.uml2.uml.PackageableElement create_artifact(org.eclipse.uml2.uml.Package package_, String name) {

		EClass eClass = UMLPackage.Literals.ARTIFACT;
		org.eclipse.uml2.uml.PackageableElement artifact_ = package_.createPackagedElement(name, eClass);

		out("Artifact %s created.", name);
		
		return artifact_;
	}
	
	protected static org.eclipse.uml2.uml.PackageableElement create_device(org.eclipse.uml2.uml.Package package_, String name) {

		EClass eClass = UMLPackage.Literals.DEVICE;
		org.eclipse.uml2.uml.PackageableElement device_ = package_.createPackagedElement(name, eClass);

		out("Device %s created.", name);
		
		return device_;
	}
	
	protected static org.eclipse.uml2.uml.PackageableElement create_execution_environment(org.eclipse.uml2.uml.Package package_, String name) {

		EClass eClass = UMLPackage.Literals.EXECUTION_ENVIRONMENT;
		org.eclipse.uml2.uml.PackageableElement execution_environment_ = package_.createPackagedElement(name, eClass);

		out("Execution environment %s created.", name);
		
		return execution_environment_;
	}
	
	protected static org.eclipse.uml2.uml.PackageableElement create_node(org.eclipse.uml2.uml.Package package_, String name) {

		EClass eClass = UMLPackage.Literals.NODE;
		org.eclipse.uml2.uml.PackageableElement node_ = package_.createPackagedElement(name, eClass);

		out("Node %s created.", name);
		
		return node_;
	}
	
	protected static org.eclipse.uml2.uml.PackageableElement create_component(org.eclipse.uml2.uml.Package package_, String name) {

		EClass eClass = UMLPackage.Literals.COMPONENT;
		org.eclipse.uml2.uml.PackageableElement component_ = package_.createPackagedElement(name, eClass);

		out("Component %s created.", name);
		
		return component_;
	}
	
	protected static org.eclipse.uml2.uml.PackageableElement create_interface(org.eclipse.uml2.uml.Package package_, String name) {

		EClass eClass = UMLPackage.Literals.INTERFACE;
		org.eclipse.uml2.uml.PackageableElement interface_ = package_.createPackagedElement(name, eClass);

		out("Interface %s created.", name);
		
		return interface_;
	}
	
	/*
	 * Methods for creating edges in diagrams
	 * 
	 * */
	protected static void create_edges_InDiagram(org.eclipse.uml2.uml.Package package_, int edges_num) {

		int i, len, index_1, index_2;
		Type t1, t2;
		EList<Type> elist = package_.getOwnedTypes();
		len = elist.size();
		for(i = 0;i<edges_num;i++) {
			index_1 = (int)(Math.random()*len);
			index_2 = (int)(Math.random()*len);
			t1 = elist.get(index_1);
			t2 = elist.get(index_2);
			createAssociation(t1,
					true, AggregationKind.NONE_LITERAL,
					t1.getName(), 0, 1,
					t2, true, AggregationKind.NONE_LITERAL, t2.getName(), 0, 1);
			
		}
		
	}
	
	
	
	protected static org.eclipse.uml2.uml.Class createClass(
			org.eclipse.uml2.uml.Package package_, String name,
			boolean isAbstract) {

		org.eclipse.uml2.uml.Class class_ = package_.createOwnedClass(name,
			isAbstract);

		out("Class '%s' created.", class_.getQualifiedName());

		return class_;
	}

	protected static Generalization createGeneralization(
			Classifier specificClassifier, Classifier generalClassifier) {

		Generalization generalization = specificClassifier
			.createGeneralization(generalClassifier);

		out("Generalization %s --|> %s created.",
			specificClassifier.getQualifiedName(),
			generalClassifier.getQualifiedName());

		return generalization;
	}

	protected static Property createAttribute(
			org.eclipse.uml2.uml.Class class_, String name, Type type,
			int lowerBound, int upperBound) {

		Property attribute = class_.createOwnedAttribute(name, type,
			lowerBound, upperBound);

		out("Attribute '%s' : %s [%s..%s] created.", //
			attribute.getQualifiedName(), // attribute name
			type.getQualifiedName(), // type name
			lowerBound, // no special case for multiplicity lower bound
			(upperBound == LiteralUnlimitedNatural.UNLIMITED)
				? "*" // special case for unlimited bound
				: upperBound);

		return attribute;
	}

	protected static Association createAssociation(Type type1,
			boolean end1IsNavigable, AggregationKind end1Aggregation,
			String end1Name, int end1LowerBound, int end1UpperBound,
			Type type2, boolean end2IsNavigable,
			AggregationKind end2Aggregation, String end2Name,
			int end2LowerBound, int end2UpperBound) {

		Association association = type1.createAssociation(end1IsNavigable,
			end1Aggregation, end1Name, end1LowerBound, end1UpperBound, type2,
			end2IsNavigable, end2Aggregation, end2Name, end2LowerBound,
			end2UpperBound);

		out("Association %s [%s..%s] %s-%s %s [%s..%s] created.", //
			UML2Util.isEmpty(end1Name)
				// compute a placeholder for the name
				? String.format("{%s}", type1.getQualifiedName()) //
				// user-specified name
				: String.format("'%s::%s'", type1.getQualifiedName(), end1Name), //
			end1LowerBound, // no special case for this
			(end1UpperBound == LiteralUnlimitedNatural.UNLIMITED)
				? "*" // special case for unlimited upper bound
				: end1UpperBound, // finite upper bound
			end2IsNavigable
				? "<" // indicate navigability
				: "-", // not navigable
			end1IsNavigable
				? ">" // indicate navigability
				: "-", // not navigable
			UML2Util.isEmpty(end2Name)
				// compute a placeholder for the name
				? String.format("{%s}", type2.getQualifiedName()) //
				// user-specified name
				: String.format("'%s::%s'", type2.getQualifiedName(), end2Name), //
			end2LowerBound, // no special case for this
			(end2UpperBound == LiteralUnlimitedNatural.UNLIMITED)
				? "*" // special case for unlimited upper bound
				: end2UpperBound);

		return association;
	}
	
	/*
	 * Methods for generating package program
	 */
	
	protected static void generate_package_diagram(int num, org.eclipse.uml2.uml.Package package_) {
		int i, index, random;
		int[] end1 = new int[2];
		int[] end2 = new int[2];
		String type;
		ArrayList<String> edge_types = new ArrayList<String>();
		ArrayList<org.eclipse.uml2.uml.PackageableElement> packages = new ArrayList<org.eclipse.uml2.uml.PackageableElement>();
		ArrayList<org.eclipse.uml2.uml.PackageableElement> models = new ArrayList<org.eclipse.uml2.uml.PackageableElement>();
		
		edge_types.add("Dependency");
		//edges in package diagram
		for(i = 0;i < num; i++) {
			index = (int)(Math.random()*edge_types.size());
			type = edge_types.get(index);
			for(int j : end1) j=-1;
			for(int j : end2) j=-1;
			
			if(type.equals("Dependency")) {
				random = (int)(Math.random()*2);
				switch(random) {
					case 0 :if(packages.size() == 0) {
								org.eclipse.uml2.uml.PackageableElement p = create_package(package_, "package_"+packages.size());
								packages.add(p);
								end1[1] = 0;
							}
							else end1[1] = (int)(Math.random()*packages.size());
							end1[0] = random; break;
					case 1 :if(models.size() == 0) {
								org.eclipse.uml2.uml.PackageableElement m = create_model(package_, "model_"+models.size());
								models.add(m);
								end1[1] = 0;
							}
							else end1[1] = (int)(Math.random()*models.size());
							end1[0] = random; break;
					
					default: break;
				}
				
				random = (int)(Math.random()*2);
				switch(random) {
					case 0 :if(packages.size() == 0) {
								org.eclipse.uml2.uml.PackageableElement p = create_package(package_, "package_"+packages.size());
								packages.add(p);
								end2[1] = 0;
							}
							else end2[1] = (int)(Math.random()*packages.size());
							end2[0] = random; break;
					case 1 :if(models.size() == 0) {
								org.eclipse.uml2.uml.PackageableElement m = create_model(package_, "model_"+models.size());
								models.add(m);
								end2[1] = 0;
							}
							else end2[1] = (int)(Math.random()*models.size());
							end2[0] = random; break;
					
					default: break;
				}
				if (end1[0] == 0) {
					if(end2[0] == 0) packages.get(end1[1]).createDependency(packages.get(end2[1]));
					else packages.get(end1[1]).createDependency(models.get(end2[1]));
				}else {
					if(end2[0] == 0) models.get(end1[1]).createDependency(packages.get(end2[1]));
					else models.get(end1[1]).createDependency(models.get(end2[1]));
				}
				
			}
		}
		for(i = 0;i < 2;i++) {
			switch(i) {
			case 0:
				for(org.eclipse.uml2.uml.PackageableElement pe:packages) {
					random = (int)(Math.random()*2);
					Comment cmt = pe.createOwnedComment();
					if(random == 0) cmt.setBody("{\"exposure\":true}");
					else cmt.setBody("{\n"
							+ "\"value\":2\n"
							+ "}");
				}
				break;
			case 1:
				for(org.eclipse.uml2.uml.PackageableElement pe:models) {
					random = (int)(Math.random()*2);
					Comment cmt = pe.createOwnedComment();
					if(random == 0) cmt.setBody("{\"exposure\":true}");
					else cmt.setBody("{\n"
							+ "\"value\":2\n"
							+ "}");
				}
				break;
			}
		}
		System.out.println("Package diagram created.");
		
	}
	
	/*
	 * 
	 */
	protected static void generate_deployment_diagram(int num, org.eclipse.uml2.uml.Package package_) {
		int i, index, random1, random2, index1, index2;
		String type;
		ArrayList<String> edge_types = new ArrayList<String>();
		
		ArrayList<org.eclipse.uml2.uml.Package> packages = new ArrayList<org.eclipse.uml2.uml.Package>();
		ArrayList<Model> models = new ArrayList<Model>();
		ArrayList<Artifact> artifacts = new ArrayList<Artifact>();
		ArrayList<Device> devices = new ArrayList<Device>();
		ArrayList<ExecutionEnvironment> execution_environments = new ArrayList<ExecutionEnvironment>();
		ArrayList<Node> nodes = new ArrayList<Node>();
		
		edge_types.add("CommunicationPath");
		edge_types.add("Dependency");
		edge_types.add("Deployment");
		edge_types.add("Generalization");
		edge_types.add("Association");
		
		for(i = 0;i < num;i++) {
			index = (int)(Math.random()*edge_types.size());
			type = edge_types.get(index);
			
			if (type.equals("CommunicationPath")) {
				while(nodes.size() < 2) {
					Node n = (Node)create_node(package_, "node_"+nodes.size());
					nodes.add(n);
				}
				index1 = (int)(Math.random()*nodes.size());
				index2 = (int)(Math.random()*nodes.size());
				nodes.get(index1).createCommunicationPath(true, AggregationKind.NONE_LITERAL, "node_"+index1, 0, 1, nodes.get(index2), true, AggregationKind.NONE_LITERAL, "node_"+index2, 0, 1);
			}
			else if(type.equals("Dependency")) {
				random1 = (int)(Math.random()*6);
				switch(random1) {
					case 0: 
						if(devices.size() == 0) {
							Device d = (Device) create_device(package_, "device_"+devices.size());
							devices.add(d);
							index1 = 0;
						}
						else index1 = (int)(Math.random()*devices.size());
						random2 = (int)(Math.random()*6);
						switch(random2) {
							case 0:
								if(devices.size() == 0) {
									Device d = (Device) create_device(package_, "device_"+devices.size());
									devices.add(d);
									index2 = 0;
								}
								else index2 = (int)(Math.random()*devices.size());
								devices.get(index1).createDependency(devices.get(index2));
								break;
							case 1:
								if(nodes.size() == 0) {
									Node n = (Node)create_node(package_, "node_"+nodes.size());
									nodes.add(n);
									index2 = 0;
								}
								else index2 = (int)(Math.random()*nodes.size());
								devices.get(index1).createDependency(nodes.get(index2));
								break;
							case 2:
								if(execution_environments.size() == 0) {
									ExecutionEnvironment e = (ExecutionEnvironment)create_execution_environment(package_, "execution_environment_"+execution_environments.size());
									execution_environments.add(e);
									index2 = 0;
								}
								else index2 = (int)(Math.random()*execution_environments.size());
								devices.get(index1).createDependency(execution_environments.get(index2));
								break;
							case 3:
								if(artifacts.size() == 0) {
									Artifact a = (Artifact) create_artifact(package_, "artifact_"+artifacts.size());
									artifacts.add(a);
									index2 = 0;
								}
								else index2 = (int)(Math.random()*artifacts.size());
								devices.get(index1).createDependency(artifacts.get(index2));
								break;
							case 4:
								if(packages.size() == 0) {
									org.eclipse.uml2.uml.Package p = (org.eclipse.uml2.uml.Package) create_package(package_, "package_"+packages.size());
									packages.add(p);
									index2 = 0;
								}
								else index2 = (int)(Math.random()*packages.size());
								devices.get(index1).createDependency(packages.get(index2));
								break;
							case 5:
								if(models.size() == 0) {
									Model m = (Model) create_model(package_, "model_"+models.size());
									models.add(m);
									index2 = 0;
								}
								else index2 = (int)(Math.random()*models.size());
								devices.get(index1).createDependency(models.get(index2));
								break;
						}
						break;
					case 1: 
						if(nodes.size() == 0) {
							Node n = (Node)create_node(package_, "node_"+nodes.size());
							nodes.add(n);
							index1 = 0;
						}
						else index1 = (int)(Math.random()*nodes.size());
						random2 = (int)(Math.random()*6);
						switch(random2) {
							case 0:
								if(devices.size() == 0) {
									Device d = (Device) create_device(package_, "device_"+devices.size());
									devices.add(d);
									index2 = 0;
								}
								else index2 = (int)(Math.random()*devices.size());
								nodes.get(index1).createDependency(devices.get(index2));
								break;
							case 1:
								if(nodes.size() == 0) {
									Node n = (Node)create_node(package_, "node_"+nodes.size());
									nodes.add(n);
									index2 = 0;
								}
								else index2 = (int)(Math.random()*nodes.size());
								nodes.get(index1).createDependency(nodes.get(index2));
								break;
							case 2:
								if(execution_environments.size() == 0) {
									ExecutionEnvironment e = (ExecutionEnvironment)create_execution_environment(package_, "execution_environment_"+execution_environments.size());
									execution_environments.add(e);
									index2 = 0;
								}
								else index2 = (int)(Math.random()*execution_environments.size());
								nodes.get(index1).createDependency(execution_environments.get(index2));
								break;
							case 3:
								if(artifacts.size() == 0) {
									Artifact a = (Artifact) create_artifact(package_, "artifact_"+artifacts.size());
									artifacts.add(a);
									index2 = 0;
								}
								else index2 = (int)(Math.random()*artifacts.size());
								nodes.get(index1).createDependency(artifacts.get(index2));
								break;
							case 4:
								if(packages.size() == 0) {
									org.eclipse.uml2.uml.Package p = (org.eclipse.uml2.uml.Package) create_package(package_, "package_"+packages.size());
									packages.add(p);
									index2 = 0;
								}
								else index2 = (int)(Math.random()*packages.size());
								nodes.get(index1).createDependency(packages.get(index2));
								break;
							case 5:
								if(models.size() == 0) {
									Model m = (Model) create_model(package_, "model_"+models.size());
									models.add(m);
									index2 = 0;
								}
								else index2 = (int)(Math.random()*models.size());
								nodes.get(index1).createDependency(models.get(index2));
								break;
						}
						break;
					case 2: 
						if(execution_environments.size() == 0) {
							ExecutionEnvironment e = (ExecutionEnvironment)create_execution_environment(package_, "execution_environment_"+execution_environments.size());
							execution_environments.add(e);
							index1 = 0;
						}
						else index1 = (int)(Math.random()*execution_environments.size());
						random2 = (int)(Math.random()*6);
						switch(random2) {
							case 0:
								if(devices.size() == 0) {
									Device d = (Device) create_device(package_, "device_"+devices.size());
									devices.add(d);
									index2 = 0;
								}
								else index2 = (int)(Math.random()*devices.size());
								execution_environments.get(index1).createDependency(devices.get(index2));
								break;
							case 1:
								if(nodes.size() == 0) {
									Node n = (Node)create_node(package_, "node_"+nodes.size());
									nodes.add(n);
									index2 = 0;
								}
								else index2 = (int)(Math.random()*nodes.size());
								execution_environments.get(index1).createDependency(nodes.get(index2));
								break;
							case 2:
								if(execution_environments.size() == 0) {
									ExecutionEnvironment e = (ExecutionEnvironment)create_execution_environment(package_, "execution_environment_"+execution_environments.size());
									execution_environments.add(e);
									index2 = 0;
								}
								else index2 = (int)(Math.random()*execution_environments.size());
								execution_environments.get(index1).createDependency(execution_environments.get(index2));
								break;
							case 3:
								if(artifacts.size() == 0) {
									Artifact a = (Artifact) create_artifact(package_, "artifact_"+artifacts.size());
									artifacts.add(a);
									index2 = 0;
								}
								else index2 = (int)(Math.random()*artifacts.size());
								execution_environments.get(index1).createDependency(artifacts.get(index2));
								break;
							case 4:
								if(packages.size() == 0) {
									org.eclipse.uml2.uml.Package p = (org.eclipse.uml2.uml.Package) create_package(package_, "package_"+packages.size());
									packages.add(p);
									index2 = 0;
								}
								else index2 = (int)(Math.random()*packages.size());
								execution_environments.get(index1).createDependency(packages.get(index2));
								break;
							case 5:
								if(models.size() == 0) {
									Model m = (Model) create_model(package_, "model_"+models.size());
									models.add(m);
									index2 = 0;
								}
								else index2 = (int)(Math.random()*models.size());
								execution_environments.get(index1).createDependency(models.get(index2));
								break;
						}
						break;
					case 3:
						if(artifacts.size() == 0) {
							Artifact a = (Artifact) create_artifact(package_, "artifact_"+artifacts.size());
							artifacts.add(a);
							index1 = 0;
						}
						else index1 = (int)(Math.random()*artifacts.size());
						random2 = (int)(Math.random()*6);
						switch(random2) {
							case 0:
								if(devices.size() == 0) {
									Device d = (Device) create_device(package_, "device_"+devices.size());
									devices.add(d);
									index2 = 0;
								}
								else index2 = (int)(Math.random()*devices.size());
								artifacts.get(index1).createDependency(devices.get(index2));
								break;
							case 1:
								if(nodes.size() == 0) {
									Node n = (Node)create_node(package_, "node_"+nodes.size());
									nodes.add(n);
									index2 = 0;
								}
								else index2 = (int)(Math.random()*nodes.size());
								artifacts.get(index1).createDependency(nodes.get(index2));
								break;
							case 2:
								if(execution_environments.size() == 0) {
									ExecutionEnvironment e = (ExecutionEnvironment)create_execution_environment(package_, "execution_environment_"+execution_environments.size());
									execution_environments.add(e);
									index2 = 0;
								}
								else index2 = (int)(Math.random()*execution_environments.size());
								artifacts.get(index1).createDependency(execution_environments.get(index2));
								break;
							case 3:
								if(artifacts.size() == 0) {
									Artifact a = (Artifact) create_artifact(package_, "artifact_"+artifacts.size());
									artifacts.add(a);
									index2 = 0;
								}
								else index2 = (int)(Math.random()*artifacts.size());
								artifacts.get(index1).createDependency(artifacts.get(index2));
								break;
							case 4:
								if(packages.size() == 0) {
									org.eclipse.uml2.uml.Package p = (org.eclipse.uml2.uml.Package) create_package(package_, "package_"+packages.size());
									packages.add(p);
									index2 = 0;
								}
								else index2 = (int)(Math.random()*packages.size());
								artifacts.get(index1).createDependency(packages.get(index2));
								break;
							case 5:
								if(models.size() == 0) {
									Model m = (Model) create_model(package_, "model_"+models.size());
									models.add(m);
									index2 = 0;
								}
								else index2 = (int)(Math.random()*models.size());
								artifacts.get(index1).createDependency(models.get(index2));
								break;
						}
						break;
					case 4:
						if(packages.size() == 0) {
							org.eclipse.uml2.uml.Package p = (org.eclipse.uml2.uml.Package) create_package(package_, "package_"+packages.size());
							packages.add(p);
							index1 = 0;
						}
						else index1 = (int)(Math.random()*packages.size());
						random2 = (int)(Math.random()*6);
						switch(random2) {
							case 0:
								if(devices.size() == 0) {
									Device d = (Device) create_device(package_, "device_"+devices.size());
									devices.add(d);
									index2 = 0;
								}
								else index2 = (int)(Math.random()*devices.size());
								packages.get(index1).createDependency(devices.get(index2));
								break;
							case 1:
								if(nodes.size() == 0) {
									Node n = (Node)create_node(package_, "node_"+nodes.size());
									nodes.add(n);
									index2 = 0;
								}
								else index2 = (int)(Math.random()*nodes.size());
								packages.get(index1).createDependency(nodes.get(index2));
								break;
							case 2:
								if(execution_environments.size() == 0) {
									ExecutionEnvironment e = (ExecutionEnvironment)create_execution_environment(package_, "execution_environment_"+execution_environments.size());
									execution_environments.add(e);
									index2 = 0;
								}
								else index2 = (int)(Math.random()*execution_environments.size());
								packages.get(index1).createDependency(execution_environments.get(index2));
								break;
							case 3:
								if(artifacts.size() == 0) {
									Artifact a = (Artifact) create_artifact(package_, "artifact_"+artifacts.size());
									artifacts.add(a);
									index2 = 0;
								}
								else index2 = (int)(Math.random()*artifacts.size());
								packages.get(index1).createDependency(artifacts.get(index2));
								break;
							case 4:
								if(packages.size() == 0) {
									org.eclipse.uml2.uml.Package p = (org.eclipse.uml2.uml.Package) create_package(package_, "package_"+packages.size());
									packages.add(p);
									index2 = 0;
								}
								else index2 = (int)(Math.random()*packages.size());
								packages.get(index1).createDependency(packages.get(index2));
								break;
							case 5:
								if(models.size() == 0) {
									Model m = (Model) create_model(package_, "model_"+models.size());
									models.add(m);
									index2 = 0;
								}
								else index2 = (int)(Math.random()*models.size());
								packages.get(index1).createDependency(models.get(index2));
								break;
						}
						break;
					case 5:
						if(models.size() == 0) {
							Model m = (Model) create_model(package_, "model_"+models.size());
							models.add(m);
							index1 = 0;
						}
						else index1 = (int)(Math.random()*models.size());
						random2 = (int)(Math.random()*6);
						switch(random2) {
							case 0:
								if(devices.size() == 0) {
									Device d = (Device) create_device(package_, "device_"+devices.size());
									devices.add(d);
									index2 = 0;
								}
								else index2 = (int)(Math.random()*devices.size());
								models.get(index1).createDependency(devices.get(index2));
								break;
							case 1:
								if(nodes.size() == 0) {
									Node n = (Node)create_node(package_, "node_"+nodes.size());
									nodes.add(n);
									index2 = 0;
								}
								else index2 = (int)(Math.random()*nodes.size());
								models.get(index1).createDependency(nodes.get(index2));
								break;
							case 2:
								if(execution_environments.size() == 0) {
									ExecutionEnvironment e = (ExecutionEnvironment)create_execution_environment(package_, "execution_environment_"+execution_environments.size());
									execution_environments.add(e);
									index2 = 0;
								}
								else index2 = (int)(Math.random()*execution_environments.size());
								models.get(index1).createDependency(execution_environments.get(index2));
								break;
							case 3:
								if(artifacts.size() == 0) {
									Artifact a = (Artifact) create_artifact(package_, "artifact_"+artifacts.size());
									artifacts.add(a);
									index2 = 0;
								}
								else index2 = (int)(Math.random()*artifacts.size());
								models.get(index1).createDependency(artifacts.get(index2));
								break;
							case 4:
								if(packages.size() == 0) {
									org.eclipse.uml2.uml.Package p = (org.eclipse.uml2.uml.Package) create_package(package_, "package_"+packages.size());
									packages.add(p);
									index2 = 0;
								}
								else index2 = (int)(Math.random()*packages.size());
								models.get(index1).createDependency(packages.get(index2));
								break;
							case 5:
								if(models.size() == 0) {
									Model m = (Model) create_model(package_, "model_"+models.size());
									models.add(m);
									index2 = 0;
								}
								else index2 = (int)(Math.random()*models.size());
								models.get(index1).createDependency(models.get(index2));
								break;
						}
						break;
					default: break;
				}
			}
			else if(type.equals("Deployment")) {
				random1 = (int)(Math.random()*3);
				switch(random1) {
					case 0: 
						if(devices.size() == 0) {
							Device d = (Device) create_device(package_, "device_"+devices.size());
							devices.add(d);
							index1 = 0;
						}
						else index1 = (int)(Math.random()*devices.size());
						devices.get(index1).createDeployment("deployment_"+i);
						break;
					case 1: 
						if(nodes.size() == 0) {
							Node n = (Node)create_node(package_, "node_"+nodes.size());
							nodes.add(n);
							index1 = 0;
						}
						else index1 = (int)(Math.random()*nodes.size());
						nodes.get(index1).createDeployment("deployment_"+i);
						break;
					case 2: 
						if(execution_environments.size() == 0) {
							ExecutionEnvironment e = (ExecutionEnvironment)create_execution_environment(package_, "execution_environment_"+execution_environments.size());
							execution_environments.add(e);
							index1 = 0;
						}
						else index1 = (int)(Math.random()*execution_environments.size());
						execution_environments.get(index1).createDeployment("deployment_"+i);
						break;
					default: break;
				}
			}
			else if(type.equals("Generalization")) {
				random1 = (int)(Math.random()*4);
				switch(random1) {
					case 0: 
						if(devices.size() == 0) {
							Device d = (Device) create_device(package_, "device_"+devices.size());
							devices.add(d);
							index1 = 0;
						}
						else index1 = (int)(Math.random()*devices.size());
						random2 = (int)(Math.random()*4);
						switch(random2) {
							case 0 :
								if(devices.size() == 0) {
									Device d = (Device) create_device(package_, "device_"+devices.size());
									devices.add(d);
									index2 = 0;
								}
								else index2 = (int)(Math.random()*devices.size());
								devices.get(index1).createGeneralization(devices.get(index2));
								break;
							case 1:
								if(nodes.size() == 0) {
									Node n = (Node)create_node(package_, "node_"+nodes.size());
									nodes.add(n);
									index2 = 0;
								}
								else index2 = (int)(Math.random()*nodes.size());
								devices.get(index1).createGeneralization(nodes.get(index2));
								break;
							case 2:
								if(execution_environments.size() == 0) {
									ExecutionEnvironment e = (ExecutionEnvironment)create_execution_environment(package_, "execution_environment_"+execution_environments.size());
									execution_environments.add(e);
									index2 = 0;
								}
								else index2 = (int)(Math.random()*execution_environments.size());
								devices.get(index1).createGeneralization(execution_environments.get(index2));
								break;
							case 3:
								if(artifacts.size() == 0) {
									Artifact a = (Artifact)create_artifact(package_, "artifact_"+artifacts.size());
									artifacts.add(a);
									index2 = 0;
								}
								else index2 = (int)(Math.random()*artifacts.size());
								devices.get(index1).createGeneralization(artifacts.get(index2));
								break;
						}
						break;
					case 1: 
						if(nodes.size() == 0) {
							Node n = (Node)create_node(package_, "node_"+nodes.size());
							nodes.add(n);
							index1 = 0;
						}
						else index1 = (int)(Math.random()*nodes.size());
						random2 = (int)(Math.random()*4);
						switch(random2) {
							case 0 :
								if(devices.size() == 0) {
									Device d = (Device) create_device(package_, "device_"+devices.size());
									devices.add(d);
									index2 = 0;
								}
								else index2 = (int)(Math.random()*devices.size());
								nodes.get(index1).createGeneralization(devices.get(index2));
								break;
							case 1:
								if(nodes.size() == 0) {
									Node n = (Node)create_node(package_, "node_"+nodes.size());
									nodes.add(n);
									index2 = 0;
								}
								else index2 = (int)(Math.random()*nodes.size());
								nodes.get(index1).createGeneralization(nodes.get(index2));
								break;
							case 2:
								if(execution_environments.size() == 0) {
									ExecutionEnvironment e = (ExecutionEnvironment)create_execution_environment(package_, "execution_environment_"+execution_environments.size());
									execution_environments.add(e);
									index2 = 0;
								}
								else index2 = (int)(Math.random()*execution_environments.size());
								nodes.get(index1).createGeneralization(execution_environments.get(index2));
								break;
							case 3:
								if(artifacts.size() == 0) {
									Artifact a = (Artifact)create_artifact(package_, "artifact_"+artifacts.size());
									artifacts.add(a);
									index2 = 0;
								}
								else index2 = (int)(Math.random()*artifacts.size());
								nodes.get(index1).createGeneralization(artifacts.get(index2));
								break;
						}
						break;
					case 2: 
						if(execution_environments.size() == 0) {
							ExecutionEnvironment e = (ExecutionEnvironment)create_execution_environment(package_, "execution_environment_"+execution_environments.size());
							execution_environments.add(e);
							index1 = 0;
						}
						else index1 = (int)(Math.random()*execution_environments.size());
						random2 = (int)(Math.random()*4);
						switch(random2) {
							case 0 :
								if(devices.size() == 0) {
									Device d = (Device) create_device(package_, "device_"+devices.size());
									devices.add(d);
									index2 = 0;
								}
								else index2 = (int)(Math.random()*devices.size());
								execution_environments.get(index1).createGeneralization(devices.get(index2));
								break;
							case 1:
								if(nodes.size() == 0) {
									Node n = (Node)create_node(package_, "node_"+nodes.size());
									nodes.add(n);
									index2 = 0;
								}
								else index2 = (int)(Math.random()*nodes.size());
								execution_environments.get(index1).createGeneralization(nodes.get(index2));
								break;
							case 2:
								if(execution_environments.size() == 0) {
									ExecutionEnvironment e = (ExecutionEnvironment)create_execution_environment(package_, "execution_environment_"+execution_environments.size());
									execution_environments.add(e);
									index2 = 0;
								}
								else index2 = (int)(Math.random()*execution_environments.size());
								execution_environments.get(index1).createGeneralization(execution_environments.get(index2));
								break;
							case 3:
								if(artifacts.size() == 0) {
									Artifact a = (Artifact)create_artifact(package_, "artifact_"+artifacts.size());
									artifacts.add(a);
									index2 = 0;
								}
								else index2 = (int)(Math.random()*artifacts.size());
								execution_environments.get(index1).createGeneralization(artifacts.get(index2));
								break;
						}
						break;
					case 3: 
						if(artifacts.size() == 0) {
							Artifact a = (Artifact)create_artifact(package_, "artifact_"+artifacts.size());
							artifacts.add(a);
							index1 = 0;
						}
						else index1 = (int)(Math.random()*artifacts.size());
						random2 = (int)(Math.random()*4);
						switch(random2) {
							case 0 :
								if(devices.size() == 0) {
									Device d = (Device) create_device(package_, "device_"+devices.size());
									devices.add(d);
									index2 = 0;
								}
								else index2 = (int)(Math.random()*devices.size());
								artifacts.get(index1).createGeneralization(devices.get(index2));
								break;
							case 1:
								if(nodes.size() == 0) {
									Node n = (Node)create_node(package_, "node_"+nodes.size());
									nodes.add(n);
									index2 = 0;
								}
								else index2 = (int)(Math.random()*nodes.size());
								artifacts.get(index1).createGeneralization(nodes.get(index2));
								break;
							case 2:
								if(execution_environments.size() == 0) {
									ExecutionEnvironment e = (ExecutionEnvironment)create_execution_environment(package_, "execution_environment_"+execution_environments.size());
									execution_environments.add(e);
									index2 = 0;
								}
								else index2 = (int)(Math.random()*execution_environments.size());
								artifacts.get(index1).createGeneralization(execution_environments.get(index2));
								break;
							case 3:
								if(artifacts.size() == 0) {
									Artifact a = (Artifact)create_artifact(package_, "artifact_"+artifacts.size());
									artifacts.add(a);
									index2 = 0;
								}
								else index2 = (int)(Math.random()*artifacts.size());
								artifacts.get(index1).createGeneralization(artifacts.get(index2));
								break;
						}
						break;
					default: break;
				}
			}
			else if(type.equals("Association")) {
				random1 = (int)(Math.random()*4);
				switch(random1) {
					case 0: 
						if(devices.size() == 0) {
							Device d = (Device) create_device(package_, "device_"+devices.size());
							devices.add(d);
							index1 = 0;
						}
						else index1 = (int)(Math.random()*devices.size());
						random2 = (int)(Math.random()*4);
						switch(random2) {
							case 0 :
								if(devices.size() == 0) {
									Device d = (Device) create_device(package_, "device_"+devices.size());
									devices.add(d);
									index2 = 0;
								}
								else index2 = (int)(Math.random()*devices.size());
								devices.get(index1).createAssociation(true, AggregationKind.NONE_LITERAL, "device_"+index2, 0, 1, devices.get(index2), true, AggregationKind.NONE_LITERAL, "device_"+index1, 0, 1);
								break;
							case 1:
								if(nodes.size() == 0) {
									Node n = (Node)create_node(package_, "node_"+nodes.size());
									nodes.add(n);
									index2 = 0;
								}
								else index2 = (int)(Math.random()*nodes.size());
								devices.get(index1).createAssociation(true, AggregationKind.NONE_LITERAL, "node_"+index2, 0, 1, nodes.get(index2), true, AggregationKind.NONE_LITERAL, "device_"+index1, 0, 1);
								break;
							case 2:
								if(execution_environments.size() == 0) {
									ExecutionEnvironment e = (ExecutionEnvironment)create_execution_environment(package_, "execution_environment_"+execution_environments.size());
									execution_environments.add(e);
									index2 = 0;
								}
								else index2 = (int)(Math.random()*execution_environments.size());
								devices.get(index1).createAssociation(true, AggregationKind.NONE_LITERAL, "execution_environment_"+index2, 0, 1, execution_environments.get(index2), true, AggregationKind.NONE_LITERAL, "device_"+index1, 0, 1);
								break;
							case 3:
								if(artifacts.size() == 0) {
									Artifact a = (Artifact)create_artifact(package_, "artifact_"+artifacts.size());
									artifacts.add(a);
									index2 = 0;
								}
								else index2 = (int)(Math.random()*artifacts.size());
								devices.get(index1).createAssociation(true, AggregationKind.NONE_LITERAL, "artifact_"+index2, 0, 1, artifacts.get(index2), true, AggregationKind.NONE_LITERAL, "device_"+index1, 0, 1);
								break;
						}
						break;
					case 1: 
						if(nodes.size() == 0) {
							Node n = (Node)create_node(package_, "node_"+nodes.size());
							nodes.add(n);
							index1 = 0;
						}
						else index1 = (int)(Math.random()*nodes.size());
						random2 = (int)(Math.random()*4);
						switch(random2) {
						case 0 :
							if(devices.size() == 0) {
								Device d = (Device) create_device(package_, "device_"+devices.size());
								devices.add(d);
								index2 = 0;
							}
							else index2 = (int)(Math.random()*devices.size());
							nodes.get(index1).createAssociation(true, AggregationKind.NONE_LITERAL, "device_"+index2, 0, 1, devices.get(index2), true, AggregationKind.NONE_LITERAL, "device_"+index1, 0, 1);
							break;
						case 1:
							if(nodes.size() == 0) {
								Node n = (Node)create_node(package_, "node_"+nodes.size());
								nodes.add(n);
								index2 = 0;
							}
							else index2 = (int)(Math.random()*nodes.size());
							nodes.get(index1).createAssociation(true, AggregationKind.NONE_LITERAL, "node_"+index2, 0, 1, nodes.get(index2), true, AggregationKind.NONE_LITERAL, "device_"+index1, 0, 1);
							break;
						case 2:
							if(execution_environments.size() == 0) {
								ExecutionEnvironment e = (ExecutionEnvironment)create_execution_environment(package_, "execution_environment_"+execution_environments.size());
								execution_environments.add(e);
								index2 = 0;
							}
							else index2 = (int)(Math.random()*execution_environments.size());
							nodes.get(index1).createAssociation(true, AggregationKind.NONE_LITERAL, "execution_environment_"+index2, 0, 1, execution_environments.get(index2), true, AggregationKind.NONE_LITERAL, "device_"+index1, 0, 1);
							break;
						case 3:
							if(artifacts.size() == 0) {
								Artifact a = (Artifact)create_artifact(package_, "artifact_"+artifacts.size());
								artifacts.add(a);
								index2 = 0;
							}
							else index2 = (int)(Math.random()*artifacts.size());
							nodes.get(index1).createAssociation(true, AggregationKind.NONE_LITERAL, "artifact_"+index2, 0, 1, artifacts.get(index2), true, AggregationKind.NONE_LITERAL, "device_"+index1, 0, 1);
							break;
						}
						break;
					case 2: 
						if(execution_environments.size() == 0) {
							ExecutionEnvironment e = (ExecutionEnvironment)create_execution_environment(package_, "execution_environment_"+execution_environments.size());
							execution_environments.add(e);
							index1 = 0;
						}
						else index1 = (int)(Math.random()*execution_environments.size());
						random2 = (int)(Math.random()*4);
						switch(random2) {
						case 0 :
							if(devices.size() == 0) {
								Device d = (Device) create_device(package_, "device_"+devices.size());
								devices.add(d);
								index2 = 0;
							}
							else index2 = (int)(Math.random()*devices.size());
							execution_environments.get(index1).createAssociation(true, AggregationKind.NONE_LITERAL, "device_"+index2, 0, 1, devices.get(index2), true, AggregationKind.NONE_LITERAL, "device_"+index1, 0, 1);
							break;
						case 1:
							if(nodes.size() == 0) {
								Node n = (Node)create_node(package_, "node_"+nodes.size());
								nodes.add(n);
								index2 = 0;
							}
							else index2 = (int)(Math.random()*nodes.size());
							execution_environments.get(index1).createAssociation(true, AggregationKind.NONE_LITERAL, "node_"+index2, 0, 1, nodes.get(index2), true, AggregationKind.NONE_LITERAL, "device_"+index1, 0, 1);
							break;
						case 2:
							if(execution_environments.size() == 0) {
								ExecutionEnvironment e = (ExecutionEnvironment)create_execution_environment(package_, "execution_environment_"+execution_environments.size());
								execution_environments.add(e);
								index2 = 0;
							}
							else index2 = (int)(Math.random()*execution_environments.size());
							execution_environments.get(index1).createAssociation(true, AggregationKind.NONE_LITERAL, "execution_environment_"+index2, 0, 1, execution_environments.get(index2), true, AggregationKind.NONE_LITERAL, "device_"+index1, 0, 1);
							break;
						case 3:
							if(artifacts.size() == 0) {
								Artifact a = (Artifact)create_artifact(package_, "artifact_"+artifacts.size());
								artifacts.add(a);
								index2 = 0;
							}
							else index2 = (int)(Math.random()*artifacts.size());
							execution_environments.get(index1).createAssociation(true, AggregationKind.NONE_LITERAL, "artifact_"+index2, 0, 1, artifacts.get(index2), true, AggregationKind.NONE_LITERAL, "device_"+index1, 0, 1);
							break;
						}
						break;
					case 3: 
						if(artifacts.size() == 0) {
							Artifact a = (Artifact)create_artifact(package_, "artifact_"+artifacts.size());
							artifacts.add(a);
							index1 = 0;
						}
						else index1 = (int)(Math.random()*artifacts.size());
						random2 = (int)(Math.random()*4);
						switch(random2) {
						case 0 :
							if(devices.size() == 0) {
								Device d = (Device) create_device(package_, "device_"+devices.size());
								devices.add(d);
								index2 = 0;
							}
							else index2 = (int)(Math.random()*devices.size());
							artifacts.get(index1).createAssociation(true, AggregationKind.NONE_LITERAL, "device_"+index2, 0, 1, devices.get(index2), true, AggregationKind.NONE_LITERAL, "device_"+index1, 0, 1);
							break;
						case 1:
							if(nodes.size() == 0) {
								Node n = (Node)create_node(package_, "node_"+nodes.size());
								nodes.add(n);
								index2 = 0;
							}
							else index2 = (int)(Math.random()*nodes.size());
							artifacts.get(index1).createAssociation(true, AggregationKind.NONE_LITERAL, "node_"+index2, 0, 1, nodes.get(index2), true, AggregationKind.NONE_LITERAL, "device_"+index1, 0, 1);
							break;
						case 2:
							if(execution_environments.size() == 0) {
								ExecutionEnvironment e = (ExecutionEnvironment)create_execution_environment(package_, "execution_environment_"+execution_environments.size());
								execution_environments.add(e);
								index2 = 0;
							}
							else index2 = (int)(Math.random()*execution_environments.size());
							artifacts.get(index1).createAssociation(true, AggregationKind.NONE_LITERAL, "execution_environment_"+index2, 0, 1, execution_environments.get(index2), true, AggregationKind.NONE_LITERAL, "device_"+index1, 0, 1);
							break;
						case 3:
							if(artifacts.size() == 0) {
								Artifact a = (Artifact)create_artifact(package_, "artifact_"+artifacts.size());
								artifacts.add(a);
								index2 = 0;
							}
							else index2 = (int)(Math.random()*artifacts.size());
							artifacts.get(index1).createAssociation(true, AggregationKind.NONE_LITERAL, "artifact_"+index2, 0, 1, artifacts.get(index2), true, AggregationKind.NONE_LITERAL, "device_"+index1, 0, 1);
							break;
						}
						break;
					default: break;
				}
			}
			
		}//'for' ends here
		
		for(i = 0;i < 6;i++) {
			switch(i) {
			case 0:
				for(org.eclipse.uml2.uml.Package p:packages) {
					random1 = (int)(Math.random()*3);
					if(random1 > 0) {
						Comment cmt = p.createOwnedComment();
						if(random1 == 1) cmt.setBody("{\"exposure\":true}");
						else if(random1 == 2) cmt.setBody("{\n" + "\"value\":2\n" + "}");
					}
				}
				break;
			case 1:
				for(Model m:models) {
					random1 = (int)(Math.random()*3);
					if(random1 > 0) {
						Comment cmt = m.createOwnedComment();
						if(random1 == 1) cmt.setBody("{\"exposure\":true}");
						else if(random1 == 2) cmt.setBody("{\n" + "\"value\":2\n" + "}");
					}
				}
				break;
			case 2:
				for(Artifact a:artifacts) {
					random1 = (int)(Math.random()*3);
					if(random1 > 0) {
						Comment cmt = a.createOwnedComment();
						if(random1 == 1) cmt.setBody("{\"exposure\":true}");
						else if(random1 == 2) cmt.setBody("{\n" + "\"value\":2\n" + "}");
					}
				}
				break;
			case 3:
				for(Device d:devices) {
					random1 = (int)(Math.random()*3);
					if(random1 > 0) {
						Comment cmt = d.createOwnedComment();
						if(random1 == 1) cmt.setBody("{\"exposure\":true}");
						else if(random1 == 2) cmt.setBody("{\n" + "\"value\":2\n" + "}");
					}
				}
				break;
			case 4:
				for(ExecutionEnvironment e:execution_environments) {
					random1 = (int)(Math.random()*3);
					if(random1 > 0) {
						Comment cmt = e.createOwnedComment();
						if(random1 == 1) cmt.setBody("{\"exposure\":true}");
						else if(random1 == 2) cmt.setBody("{\n" + "\"value\":2\n" + "}");
					}
				}
				break;
			case 5:
				for(Node n:nodes) {
					random1 = (int)(Math.random()*3);
					if(random1 > 0) {
						Comment cmt = n.createOwnedComment();
						if(random1 == 1) cmt.setBody("{\"exposure\":true}");
						else if(random1 == 2) cmt.setBody("{\n" + "\"value\":2\n" + "}");
					}
				}
				break;
			}
		}
		System.out.println("Deployment diagram created.");
		
	}
	
	/*
	 * 
	 */
	protected static void generate_component_diagram(int num, org.eclipse.uml2.uml.Package package_) {
		int i, index, random1, random2, index1, index2;
		String type;
		ArrayList<String> edge_types = new ArrayList<String>();
		
		ArrayList<org.eclipse.uml2.uml.Package> packages = new ArrayList<org.eclipse.uml2.uml.Package>();
		ArrayList<Model> models = new ArrayList<Model>();
		ArrayList<Interface> interfaces = new ArrayList<Interface>();
		ArrayList<Component> components = new ArrayList<Component>();
		
		edge_types.add("Connector");
		edge_types.add("Dependency");
		edge_types.add("Generalization");
		edge_types.add("Association");
		
		for(i = 0;i < num;i++) {
			index = (int)(Math.random()*edge_types.size());
			type = edge_types.get(index);
			
			if (type.equals("Connector")) {
				while(components.size() < 2) {
					Component c = (Component)create_component(package_, "component_"+components.size());
					components.add(c);
				}
				index1 = (int)(Math.random()*components.size());
				index2 = (int)(Math.random()*components.size());
				components.get(index1).createOwnedConnector("connector_"+i);
			}
			else if(type.equals("Dependency")) {
				random1 = (int)(Math.random()*4);
				switch(random1) {
				case 0:
					if(packages.size() == 0) {
						org.eclipse.uml2.uml.Package p = (org.eclipse.uml2.uml.Package) create_package(package_, "package_"+packages.size());
						packages.add(p);
						index1 = 0;
					}
					else index1 = (int)(Math.random()*packages.size());
					random2 = (int)(Math.random()*4);
					switch(random2) {
					case 0:
						if(packages.size() == 0) {
							org.eclipse.uml2.uml.Package p = (org.eclipse.uml2.uml.Package) create_package(package_, "package_"+packages.size());
							packages.add(p);
							index2 = 0;
						}
						else index2 = (int)(Math.random()*packages.size());
						packages.get(index1).createDependency(packages.get(index2));
						break;
					case 1:
						if(models.size() == 0) {
							Model m = (Model) create_model(package_, "model_"+models.size());
							models.add(m);
							index2 = 0;
						}
						else index2 = (int)(Math.random()*models.size());
						packages.get(index1).createDependency(models.get(index2));
						break;
					case 2:
						if(interfaces.size() == 0) {
							Interface f = (Interface) create_interface(package_, "interface_"+interfaces.size());
							interfaces.add(f);
							index2 = 0;
						}
						else index2 = (int)(Math.random()*interfaces.size());
						packages.get(index1).createDependency(interfaces.get(index2));
						break;
					case 3:
						if(components.size() == 0) {
							Component c = (Component) create_component(package_, "component_"+components.size());
							components.add(c);
							index2 = 0;
						}
						else index2 = (int)(Math.random()*components.size());
						packages.get(index1).createDependency(components.get(index2));
						break;
					}
					break;
				case 1:
					if(models.size() == 0) {
						Model m = (Model) create_model(package_, "model_"+models.size());
						models.add(m);
						index1 = 0;
					}
					else index1 = (int)(Math.random()*models.size());
					random2 = (int)(Math.random()*4);
					switch(random2) {
					case 0:
						if(packages.size() == 0) {
							org.eclipse.uml2.uml.Package p = (org.eclipse.uml2.uml.Package) create_package(package_, "package_"+packages.size());
							packages.add(p);
							index2 = 0;
						}
						else index2 = (int)(Math.random()*packages.size());
						models.get(index1).createDependency(packages.get(index2));
						break;
					case 1:
						if(models.size() == 0) {
							Model m = (Model) create_model(package_, "model_"+models.size());
							models.add(m);
							index2 = 0;
						}
						else index2 = (int)(Math.random()*models.size());
						models.get(index1).createDependency(models.get(index2));
						break;
					case 2:
						if(interfaces.size() == 0) {
							Interface f = (Interface) create_interface(package_, "interface_"+interfaces.size());
							interfaces.add(f);
							index2 = 0;
						}
						else index2 = (int)(Math.random()*interfaces.size());
						models.get(index1).createDependency(interfaces.get(index2));
						break;
					case 3:
						if(components.size() == 0) {
							Component c = (Component) create_component(package_, "component_"+components.size());
							components.add(c);
							index2 = 0;
						}
						else index2 = (int)(Math.random()*components.size());
						models.get(index1).createDependency(components.get(index2));
						break;
					}
					break;
				case 2:
					if(interfaces.size() == 0) {
						Interface f = (Interface) create_interface(package_, "interface_"+interfaces.size());
						interfaces.add(f);
						index1 = 0;
					}
					else index1 = (int)(Math.random()*interfaces.size());
					random2 = (int)(Math.random()*4);
					switch(random2) {
					case 0:
						if(packages.size() == 0) {
							org.eclipse.uml2.uml.Package p = (org.eclipse.uml2.uml.Package) create_package(package_, "package_"+packages.size());
							packages.add(p);
							index2 = 0;
						}
						else index2 = (int)(Math.random()*packages.size());
						interfaces.get(index1).createDependency(packages.get(index2));
						break;
					case 1:
						if(models.size() == 0) {
							Model m = (Model) create_model(package_, "model_"+models.size());
							models.add(m);
							index2 = 0;
						}
						else index2 = (int)(Math.random()*models.size());
						interfaces.get(index1).createDependency(models.get(index2));
						break;
					case 2:
						if(interfaces.size() == 0) {
							Interface f = (Interface) create_interface(package_, "interface_"+interfaces.size());
							interfaces.add(f);
							index2 = 0;
						}
						else index2 = (int)(Math.random()*interfaces.size());
						interfaces.get(index1).createDependency(interfaces.get(index2));
						break;
					case 3:
						if(components.size() == 0) {
							Component c = (Component) create_component(package_, "component_"+components.size());
							components.add(c);
							index2 = 0;
						}
						else index2 = (int)(Math.random()*components.size());
						interfaces.get(index1).createDependency(components.get(index2));
						break;
					}
					break;
				case 3:
					if(components.size() == 0) {
						Component c = (Component) create_component(package_, "component_"+components.size());
						components.add(c);
						index1 = 0;
					}
					else index1 = (int)(Math.random()*components.size());
					random2 = (int)(Math.random()*4);
					switch(random2) {
					case 0:
						if(packages.size() == 0) {
							org.eclipse.uml2.uml.Package p = (org.eclipse.uml2.uml.Package) create_package(package_, "package_"+packages.size());
							packages.add(p);
							index2 = 0;
						}
						else index2 = (int)(Math.random()*packages.size());
						components.get(index1).createDependency(packages.get(index2));
						break;
					case 1:
						if(models.size() == 0) {
							Model m = (Model) create_model(package_, "model_"+models.size());
							models.add(m);
							index2 = 0;
						}
						else index2 = (int)(Math.random()*models.size());
						components.get(index1).createDependency(models.get(index2));
						break;
					case 2:
						if(interfaces.size() == 0) {
							Interface f = (Interface) create_interface(package_, "interface_"+interfaces.size());
							interfaces.add(f);
							index2 = 0;
						}
						else index2 = (int)(Math.random()*interfaces.size());
						components.get(index1).createDependency(interfaces.get(index2));
						break;
					case 3:
						if(components.size() == 0) {
							Component c = (Component) create_component(package_, "component_"+components.size());
							components.add(c);
							index2 = 0;
						}
						else index2 = (int)(Math.random()*components.size());
						components.get(index1).createDependency(components.get(index2));
						break;
					}
					break;
				default:break;
				}
			}
			else if(type.equals("Generalization")) {
				random1 = (int)(Math.random()*2);
				switch(random1) {
				case 0:
					if(interfaces.size() == 0) {
						Interface f = (Interface) create_interface(package_, "interface_"+interfaces.size());
						interfaces.add(f);
						index1 = 0;
					}
					else index1 = (int)(Math.random()*interfaces.size());
					random2 = (int)(Math.random()*2);
					switch(random2) {
					case 0:
						if(interfaces.size() == 0) {
							Interface f = (Interface) create_interface(package_, "interface_"+interfaces.size());
							interfaces.add(f);
							index2 = 0;
						}
						else index2 = (int)(Math.random()*interfaces.size());
						interfaces.get(index1).createGeneralization(interfaces.get(index2));
						break;
					case 1:
						if(components.size() == 0) {
							Component c = (Component) create_component(package_, "component_"+components.size());
							components.add(c);
							index2 = 0;
						}
						else index2 = (int)(Math.random()*components.size());
						interfaces.get(index1).createGeneralization(components.get(index2));
						break;
					}
					break;
				case 1:
					if(components.size() == 0) {
						Component c = (Component) create_component(package_, "component_"+components.size());
						components.add(c);
						index1 = 0;
					}
					else index1 = (int)(Math.random()*components.size());
					random2 = (int)(Math.random()*2);
					switch(random2) {
					case 0:
						if(interfaces.size() == 0) {
							Interface f = (Interface) create_interface(package_, "interface_"+interfaces.size());
							interfaces.add(f);
							index2 = 0;
						}
						else index2 = (int)(Math.random()*interfaces.size());
						components.get(index1).createGeneralization(interfaces.get(index2));
						break;
					case 1:
						if(components.size() == 0) {
							Component c = (Component) create_component(package_, "component_"+components.size());
							components.add(c);
							index2 = 0;
						}
						else index2 = (int)(Math.random()*components.size());
						components.get(index1).createGeneralization(components.get(index2));
						break;
					}
					break;
				default:break;
				}
			}
			else if(type.equals("Association")) {
				random1 = (int)(Math.random()*2);
				switch(random1) {
				case 0:
					if(interfaces.size() == 0) {
						Interface f = (Interface) create_interface(package_, "interface_"+interfaces.size());
						interfaces.add(f);
						index1 = 0;
					}
					else index1 = (int)(Math.random()*interfaces.size());
					random2 = (int)(Math.random()*2);
					switch(random2) {
					case 0:
						if(interfaces.size() == 0) {
							Interface f = (Interface) create_interface(package_, "interface_"+interfaces.size());
							interfaces.add(f);
							index2 = 0;
						}
						else index2 = (int)(Math.random()*interfaces.size());
						interfaces.get(index1).createAssociation(true, AggregationKind.NONE_LITERAL, "interface_"+index2, 0, 1, interfaces.get(index2), true, AggregationKind.NONE_LITERAL, "interface_"+index1, 0, 1);
						break;
					case 1:
						if(components.size() == 0) {
							Component c = (Component) create_component(package_, "component_"+components.size());
							components.add(c);
							index2 = 0;
						}
						else index2 = (int)(Math.random()*components.size());
						interfaces.get(index1).createAssociation(true, AggregationKind.NONE_LITERAL, "component_"+index2, 0, 1, components.get(index2), true, AggregationKind.NONE_LITERAL, "interface_"+index1, 0, 1);
						break;
					}
					break;
				case 1:
					if(components.size() == 0) {
						Component c = (Component) create_component(package_, "component_"+components.size());
						components.add(c);
						index1 = 0;
					}
					else index1 = (int)(Math.random()*components.size());
					random2 = (int)(Math.random()*2);
					switch(random2) {
					case 0:
						if(interfaces.size() == 0) {
							Interface f = (Interface) create_interface(package_, "interface_"+interfaces.size());
							interfaces.add(f);
							index2 = 0;
						}
						else index2 = (int)(Math.random()*interfaces.size());
						components.get(index1).createAssociation(true, AggregationKind.NONE_LITERAL, "interface_"+index2, 0, 1, interfaces.get(index2), true, AggregationKind.NONE_LITERAL, "component_"+index1, 0, 1);
						break;
					case 1:
						if(components.size() == 0) {
							Component c = (Component) create_component(package_, "component_"+components.size());
							components.add(c);
							index2 = 0;
						}
						else index2 = (int)(Math.random()*components.size());
						components.get(index1).createAssociation(true, AggregationKind.NONE_LITERAL, "component_"+index2, 0, 1, components.get(index2), true, AggregationKind.NONE_LITERAL, "component_"+index1, 0, 1);
						break;
					}
					break;
				default:break;
				}
			}
		}
		
		for(i = 0;i < 6;i++) {
			switch(i) {
			case 0:
				for(org.eclipse.uml2.uml.Package p:packages) {
					random1 = (int)(Math.random()*3);
					if(random1 > 0) {
						Comment cmt = p.createOwnedComment();
						if(random1 == 1) cmt.setBody("{\"exposure\":true}");
						else if(random1 == 2) cmt.setBody("{\n" + "\"value\":2\n" + "}");
					}
				}
				break;
			case 1:
				for(Model m:models) {
					random1 = (int)(Math.random()*3);
					if(random1 > 0) {
						Comment cmt = m.createOwnedComment();
						if(random1 == 1) cmt.setBody("{\"exposure\":true}");
						else if(random1 == 2) cmt.setBody("{\n" + "\"value\":2\n" + "}");
					}
				}
				break;
			case 2:
				for(Interface f:interfaces) {
					random1 = (int)(Math.random()*3);
					if(random1 > 0) {
						Comment cmt = f.createOwnedComment();
						if(random1 == 1) cmt.setBody("{\"exposure\":true}");
						else if(random1 == 2) cmt.setBody("{\n" + "\"value\":2\n" + "}");
					}
				}
				break;
			case 3:
				for(Component c:components) {
					random1 = (int)(Math.random()*3);
					if(random1 > 0) {
						Comment cmt = c.createOwnedComment();
						if(random1 == 1) cmt.setBody("{\"exposure\":true}");
						else if(random1 == 2) cmt.setBody("{\n" + "\"value\":2\n" + "}");
					}
				}
				break;
			}
		}
		System.out.println("Component diagram created.");
	}
	
	
	
	//
	// Program control
	//

	private static boolean processArgs(String[] args)
			throws IOException {

		if (args.length != 2) {
			err("Expected 2 argument.");
			err("Usage: java -jar ... %s <dir>",
				GettingStartedWithUML2.class.getSimpleName());
			err("where");
			err("<dir> - path to output folder in which to save the UML model");
			return false;
		}

		nodes_num = Integer.parseInt(args[1]);
		outputDir = new File(args[0]).getCanonicalFile();
		if (!outputDir.exists()) {
			err("No such directory: %s", outputDir.getAbsolutePath());
			return false;
		}

		if (!outputDir.isDirectory()) {
			err("Not a directory: %s", outputDir.getAbsolutePath());
			return false;
		}

		if (!outputDir.canWrite()) {
			err("Cannot create a file in directory: %s",
				outputDir.getAbsolutePath());
			return false;
		}

		return true;
	}

	protected static void save(org.eclipse.uml2.uml.Package package_, URI uri) {
		// Create a resource-set to contain the resource(s) that we are saving
		ResourceSet resourceSet = new ResourceSetImpl();

		// Initialize registrations of resource factories, library models,
		// profiles, Ecore metadata, and other dependencies required for
		// serializing and working with UML resources. This is only necessary in
		// applications that are not hosted in the Eclipse platform run-time, in
		// which case these registrations are discovered automatically from
		// Eclipse extension points.
		UMLResourcesUtil.init(resourceSet);

		// Create the output resource and add our model package to it.
		Resource resource = resourceSet.createResource(uri);
		resource.getContents().add(package_);

		// And save
		try {
			resource.save(null); // no save options needed
			out("Done.");
		} catch (IOException ioe) {
			err(ioe.getMessage());
		}
	}

	//
	// Logging utilities
	//

	protected static void banner(String format, Object... args) {
		System.out.println();
		hrule();

		System.out.printf(format, args);
		if (!format.endsWith("%n")) {
			System.out.println();
		}

		hrule();
		System.out.println();
	}

	protected static void hrule() {
		System.out.println("------------------------------------");
	}

	protected static void out(String format, Object... args) {
		if (DEBUG) {
			System.out.printf(format, args);
			if (!format.endsWith("%n")) {
				System.out.println();
			}
		}
	}

	protected static void err(String format, Object... args) {
		System.err.printf(format, args);
		if (!format.endsWith("%n")) {
			System.err.println();
		}
	}
}
