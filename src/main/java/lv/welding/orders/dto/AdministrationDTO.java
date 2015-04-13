package lv.welding.orders.dto;

import java.io.*;
import java.util.*;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

import lv.welding.orders.dao.*;
import lv.welding.orders.models.*;
import lv.welding.orders.services.UserService;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.UploadedFile;
import org.springframework.transaction.annotation.Transactional;

public class AdministrationDTO {
	private TermsDao termsDao;
	private CompanyDao companyDao;
	private ProductDao productDao;
	private CountryDao countryDao;
	private PersonDao personDao;
	private CategoryDao categoryDao;
	private ProductModelDao productModelDao;
	private ProductSizeDao productSizeDao;
	private ProductMaterialDao productMaterialDao;

	private UserService userService;
	
	private List<TermsEntity> allTerms;
	private List<ProductEntity> products;
	private List<UserEntity> users;
	private List<CompanyEntity> companies;
	private List<CountryEntity> countries;
	private List<PersonEntity> persons;
	private List<CategoryEntity> categories;

	private List<ProductEntity> categoryProducts;
    private ProductEntity[] selectedProducts;
    private ProductModel productModel;
    private ProductModel productModel1;
    private List<CategoryEntity> selectedCategories;
    private CategoryModel categoryModel;

	
	private TermsEntity terms = new TermsEntity();
	private ProductEntity product = new ProductEntity();
	private UserEntity user = new UserEntity();
	private CompanyEntity company = new CompanyEntity();
	private CountryEntity country = new CountryEntity();
	private PersonEntity person = new PersonEntity();
	private CategoryEntity category = new CategoryEntity();
	private ProductModelEntity productModelEntity = new ProductModelEntity();
	private ProductSizeEntity productSize = new ProductSizeEntity();
	private ProductMaterialEntity productMaterial = new ProductMaterialEntity();

	private CategoryEntity selectedCategory;
	private CategoryEntity selectedSubCategory;
	private CategoryEntity selectedSubCategory2;

	private ProductModelEntity selectedProductModel;
	private ProductSizeEntity selectedProductSize;
	private ProductMaterialEntity selectedProductMaterial;

    private UploadedFile categoryImage;

	boolean edit = false;
	private Integer activeIndex = 0;

	public void msg(String message) {
		FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(message));
	}
	
	public void clearFields() {
		terms = new TermsEntity();
		product = new ProductEntity();
		user = new UserEntity();
		company = new CompanyEntity();
		country = new CountryEntity();
		person = new PersonEntity();
        category = new CategoryEntity();
        productModelEntity = new ProductModelEntity();
        productSize = new ProductSizeEntity();
        productMaterial = new ProductMaterialEntity();

        categories = categoryDao.getAllCategories();
        if(selectedSubCategory2 != null)
            selectedSubCategory2 = categoryDao.getCategory(selectedSubCategory2.getName());
        selectedProducts = null;
        selectedCategories = null;
        categoryImage = null;

        edit = false;
	}
	
	public void redirectTo(String url) {
		try {
			FacesContext.getCurrentInstance().getExternalContext().redirect("/welding/app/" + url);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void failSave(String message) {
		msg(message);
		clearFields();
	}
	
	
	//-------------------------- USERS --------------------------------------
	
	
	public void addNewUser() {
		
		try {
			userService.createUserIfNotExist(user);
			msg("User " + user.getUsername() +" successfully created!");
			clearFields();
			
		} catch (Exception e) {
			e.printStackTrace();
			msg(e.getMessage());
		}
}
	
	
	public void deleteUser() {
		try {
			userService.deleteUser(user);
			msg("User " + user.getUsername() + " successfully deleted!");
			clearFields();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	
	//-------------------------- COMPANIES ----------------------------------
	
	
	public void saveCompany() {
		if(edit) {
			saveEditedCompany();
			return;
		}
		if(companyDao.getCompany(company.getName()) != null) {
			failSave("Company already in database.");
			return;
		}
		try {
			Integer.parseInt(company.getOnr());
		} catch (Exception e) {
			failSave("Last company order number must be a number less than 10 symbols");
			return;
		}
		companyDao.save(company);
		msg("Company successfully created!");
		clearFields();
	}
	
	public void saveEditedCompany() {
		companyDao.update(company);
		msg("Company successfully updated!");
		clearFields();
	}
	
	public void deleteCompany() {
		companyDao.delete(company);
		msg("Company successfully deleted!");
	}
	
	public void setCompanyForEdit(CompanyEntity c) {
		setCompany(c);
		edit = true;
	}


	//-------------------------- TERMS ------------------------------------
	
	
	
	public void saveTerms() {
		if(edit) {
			saveEditedTerms();
			return;
		}
		if(termsDao.getTerms(terms.getTerms()) != null) {
			failSave("Terms already in database."); 
			return;
		}
		termsDao.save(terms);
		msg("Delivery Terms successfully created!");
		clearFields();
	}
	
	public void saveEditedTerms() {
		termsDao.update(terms);
		msg("Delivery Terms successfully updated!");
		clearFields();
	}
	
	public void deleteTerms() {
		termsDao.delete(terms);
		msg("Delivery Terms successfully deleted!");
		clearFields();
	}
	
	public void setTermsForEdit(TermsEntity t) {
		setTerms(t);
		edit = true;
	} 
	
	public List<String> completeTerms(String query) {
		getAllTerms();
        List<String> filteredTerms = new ArrayList<String>();
        
        for(TermsEntity t: allTerms) {
        	if(t.getTerms().toUpperCase().contains(query.toUpperCase()))
        		filteredTerms.add(t.getTerms());
        }
        
        return filteredTerms;
	}
	
	
	
	
	//-------------------------- PRODUCTS ------------------------------------
	
	
	public void saveProduct() {
		if(edit) {
			saveEditedProduct();
			return;
		}
		if(productDao.getProduct(product.getPno()) != null) {
			failSave("Product already in database.");
			return;
		}
		productDao.save(product);
		clearFields();
		msg("Product successfully created!");
	}
	
	public void saveEditedProduct() {
		productDao.update(product);
		clearFields();
		msg("Product successfully edited!");
	}
	
	public void deleteProduct() {
		productDao.delete(product);
		clearFields();
		msg("Product successfully deleted!");
	}
	
	public void setProductForEdit(ProductEntity p) {
		setProduct(p);
		edit = true;
	}
	
	
	
	//-------------------------- COUNTRIES ------------------------------------
	
	
	
	public void saveCountry() {
		if(edit) {
			saveEditedCountry();
			return;
		}
		if(countryDao.getCountry(country.getName()) != null) {
			failSave("Country already in database.");
			return;
		}
		countryDao.save(country);
		clearFields();
		msg("Country successfully created!");
	}
	
	public void saveEditedCountry() {
		countryDao.update(country);
		clearFields();
		msg("Country successfully edited!");
	}
	
	public void deleteCountry() {
		countryDao.delete(country);
		clearFields();
		msg("Country successfully deleted!");
	}
	
	public void setCountryForEdit(CountryEntity c) {
		setCountry(c);
		edit = true;
	}
	
	
	
	
	
	//-------------------------- PERSONS ------------------------------------
	
	public void savePerson() {
		if(edit) {
			saveEditedPerson();
			return;
		}
		if(personDao.getPerson(person.getName()) != null) {
			failSave("Person already in database.");
			return;
		}
		personDao.save(person);
		clearFields();
		msg("Person successfully saved!");
	}
	
	public void saveEditedPerson() {
		personDao.update(person);
		clearFields();
		msg("Person successfully edited!");
	}
	
	public void deletePerson() {
		personDao.delete(person);
		clearFields();
		msg("Person successfully deleted!");
	}
	
	public void setPersonForEdit(PersonEntity p) {
		setPerson(p);
		edit = true;
	}
	
	public List<String> completePerson(String query) {
		getPersons();
        List<String> filteredPersons = new ArrayList<String>();
        
        for(PersonEntity t: persons) {
        	if(t.getName().toLowerCase().contains(query.toLowerCase()))
        		filteredPersons.add(t.getName());
        }
        
        return filteredPersons;
	}





    //-------------------------- CATEGORIES ------------------------------------


    public void saveCategory() {
        if(edit) {
            saveEditedCategory();
            return;
        }
        if(categoryDao.getCategory(category.getName()) != null) {
            failSave("Category with this name already in database.");
            return;
        }
        if(selectedCategory != null) {
            if(selectedSubCategory != null) {
                selectedSubCategory = categoryDao.getCategory(selectedSubCategory.getName());
                category.setCategory(selectedSubCategory);
                if(selectedSubCategory.getSubCategories() == null)
                    selectedSubCategory.setSubCategories(new ArrayList<CategoryEntity>());
                selectedSubCategory.getSubCategories().add(category);
                categoryDao.update(selectedSubCategory);
            }
            else {
                selectedCategory = categoryDao.getCategory(selectedCategory.getName());
                category.setCategory(selectedCategory);
                if(selectedCategory.getSubCategories() == null)
                    selectedCategory.setSubCategories(new ArrayList<CategoryEntity>());
                selectedCategory.getSubCategories().add(category);
                categoryDao.update(selectedCategory);
            }
        }
        categoryDao.update(category);
        saveImage(categoryImage);
        clearFields();
        msg("Category successfully created!");
    }

    public void saveEditedCategory() {
        categoryDao.update(category);
        clearFields();
        msg("Category successfully edited!");
    }

    public void deleteCategory() {
        categoryDao.delete(category);
        clearFields();
        msg("Category successfully deleted!");
    }

    public void setCategoryForEdit(CategoryEntity c) {
        setCategory(c);
        edit = true;
    }

    public List<CategoryEntity> getMotherCategories() {
        List<CategoryEntity> results = new ArrayList<CategoryEntity>();
        refreshCategories();
        for(CategoryEntity c: categories) {
            if(c.getCategory() == null)
                results.add(c);
        }
        return results;
    }

    public void addProducts(CategoryEntity category) {
        try {
            category = categoryDao.getCategory(category.getName());
            if(category.getProducts() == null)
                category.setProducts(new ArrayList<ProductEntity>());
            for(ProductEntity p: selectedProducts) {
                if(!category.getProducts().contains(p)) {
                    category.getProducts().add(p);
                    p.setCategory(category);
                    productDao.update(p);
                }
            }
            categoryDao.update(category);
            clearFields();
            msg("Products successfully added to category <" + category.getName() + ">");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

	public void addProductsToCurrentModel() {
        try {
            for(ProductEntity p: selectedProducts) {
				p.setProductModel(selectedProductModel);
				p.setProductSize(selectedProductSize);
				p.setProductMaterial(selectedProductMaterial);
				productDao.update(p);
            }
            clearFields();
            msg("Products successfully added");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Integer countProductsInCategory(CategoryEntity category) {
        Integer result = 0;
        if(category.getSubCategories() != null)
            for(CategoryEntity c: category.getSubCategories()) {
                result += countProductsInCategory(c);
            }
        if(category.getProducts() != null)
            result += category.getProducts().size();
        return result;
    }

    public void removeProductFromCategory(CategoryEntity category, ProductEntity product) {
        try {
            if (category == null)
                return;
            if(product != null) {
                product.setCategory(null);
                productDao.update(product);
            }
            if(category.getProducts() != null)
                category.getProducts().remove(product);
            categoryDao.update(category);
            msg("Product successfully removed from category");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void removeCategory(CategoryEntity category) {
        try {
            if (category == null)
                return;
            if (category.getSubCategories() != null) {
                for (CategoryEntity c : category.getSubCategories()) {
                    for(ProductEntity p: c.getProducts()) {
                        p.setCategory(null);
                        productDao.update(p);
                    }
//                    c.setProducts(null);
//                    categoryDao.update(c);
                    categoryDao.delete(c);
                }
            }
            if(category.getCategory() != null) {
                CategoryEntity parent = category.getCategory();
                category.getCategory().getSubCategories().remove(category);
                category.setCategory(null);
                //categoryDao.update(parent);
            }

            for(ProductEntity p: category.getProducts()) {
                p.setCategory(null);
                productDao.update(p);
            }
//            category.setProducts(null);
//            categoryDao.update(category);
            categoryDao.delete(category);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Transactional
    public void removeCategories(List<CategoryEntity> categories) {
        for(CategoryEntity category: categories) {
            removeCategory(category);
        }
        categoryDao.flush();
        clearFields();
        msg("Categories successfully deleted!");
    }

    public CategoryModel convertToCategoryModel(List<CategoryEntity> categories) {
        this.categoryModel = new CategoryModel(categories);
        return this.categoryModel;
    }

    public void refreshCategories() {
        categories = categoryDao.getAllCategories();
    }

    public void saveImage(UploadedFile image) {
        if(image == null)
            return;
        try {
            OutputStream out = new FileOutputStream("C:/Users/Rihards/Documents/welding/target/welding-0.0.1-SNAPSHOT/images/" + category.getName() + ".png");
            InputStream in = image.getInputstream();
            int read = 0;
            byte[] bytes = new byte[1024];
            while ((read = in.read(bytes)) != -1) {
                out.write(bytes, 0, read);
            }

            in.close();
            out.flush();
            out.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void uploadImage(FileUploadEvent event) {
        setCategoryImage(event.getFile());
    }







	//-------------------------- PRODUCT MODELS ------------------------------------


	public void saveProductModel() {
		if(edit) {
			saveEditedProductModel();
			return;
		}
		if(productModelDao.getProductModel(productModelEntity.getName()) != null) {
			failSave("Category with this name already in database.");
			return;
		}
		productModelDao.update(productModelEntity);
		clearFields();
		msg("Product Model successfully created!");
	}

	public void saveEditedProductModel() {
		productModelDao.update(productModelEntity);
		clearFields();
		msg("Product Model successfully edited!");
	}

//	public void deleteProductModel() {
//		categoryDao.delete(category);
//		clearFields();
//		msg("Category successfully deleted!");
//	}
//
//	public void setProductModelForEdit(ProductModelEntity c) {
//		setProductModel(c);
//		edit = true;
//	}

//	public void addProducts(CategoryEntity category) {
//		try {
//			category = categoryDao.getCategory(category.getName());
//			if(category.getProducts() == null)
//				category.setProducts(new ArrayList<ProductEntity>());
//			for(ProductEntity p: selectedProducts) {
//				if(!category.getProducts().contains(p)) {
//					category.getProducts().add(p);
//					p.setCategory(category);
//					productDao.update(p);
//				}
//			}
//			categoryDao.update(category);
//			clearFields();
//			msg("Products successfully added to category <" + category.getName() + ">");
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//	}
//
//	public Integer countProductsInCategory(CategoryEntity category) {
//		Integer result = 0;
//		if(category.getSubCategories() != null)
//			for(CategoryEntity c: category.getSubCategories()) {
//				result += countProductsInCategory(c);
//			}
//		if(category.getProducts() != null)
//			result += category.getProducts().size();
//		return result;
//	}
//
//	public void removeProductFromCategory(CategoryEntity category, ProductEntity product) {
//		try {
//			if (category == null)
//				return;
//			if(product != null) {
//				product.setCategory(null);
//				productDao.update(product);
//			}
//			if(category.getProducts() != null)
//				category.getProducts().remove(product);
//			categoryDao.update(category);
//			msg("Product successfully removed from category");
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//	}
//
//	public void removeCategory(CategoryEntity category) {
//		try {
//			if (category == null)
//				return;
//			if (category.getSubCategories() != null) {
//				for (CategoryEntity c : category.getSubCategories()) {
//					for(ProductEntity p: c.getProducts()) {
//						p.setCategory(null);
//						productDao.update(p);
//					}
////                    c.setProducts(null);
////                    categoryDao.update(c);
//					categoryDao.delete(c);
//				}
//			}
//			if(category.getCategory() != null) {
//				CategoryEntity parent = category.getCategory();
//				category.getCategory().getSubCategories().remove(category);
//				category.setCategory(null);
//				//categoryDao.update(parent);
//			}
//
//			for(ProductEntity p: category.getProducts()) {
//				p.setCategory(null);
//				productDao.update(p);
//			}
////            category.setProducts(null);
////            categoryDao.update(category);
//			categoryDao.delete(category);
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//	}







	//-------------------------- PRODUCT SIZES ------------------------------------


	public void saveProductSize() {
		if(edit) {
			saveEditedProductSize();
			return;
		}
		if(productSizeDao.getProductSize(productSize.getName()) != null) {
			failSave("Product Size with this name already in database.");
			return;
		}
		productSizeDao.update(productSize);
		clearFields();
		msg("Product Size successfully created!");
	}

	public void saveEditedProductSize() {
		productSizeDao.update(productSize);
		clearFields();
		msg("Product Size successfully edited!");
	}




	//-------------------------- PRODUCT SIZES ------------------------------------


	public void saveProductMaterial() {
		if(edit) {
			saveEditedProductMaterial();
			return;
		}
		if(productMaterialDao.getProductMaterial(productMaterial.getName()) != null) {
			failSave("Product Material with this name already in database.");
			return;
		}
		productMaterialDao.update(productMaterial);
		clearFields();
		msg("Product Material successfully created!");
	}

	public void saveEditedProductMaterial() {
		productMaterialDao.update(productMaterial);
		clearFields();
		msg("Product Material successfully edited!");
	}

	/**
	 * @since 12.04.2015
	 * @return selectedModel + selectedSize + selectedMaterial
	 */
	public String getModelPath() {
		StringBuilder result = new StringBuilder();
		if(selectedProductModel != null) {
			result.append(selectedProductModel.getName());
		}
		if(selectedProductSize != null) {
			result.append(" " + selectedProductSize.getName());
		}
		if(selectedProductMaterial != null) {
			result.append(" " + selectedProductMaterial.getName());
		}
		return result.toString();
	}

	public List<ProductEntity> getCurrentModelProducts() {
		return getModelProducts(selectedProductModel, selectedProductSize, selectedProductMaterial);
	}

	public List<ProductEntity> getModelProducts(ProductModelEntity productModel, ProductSizeEntity productSize, ProductMaterialEntity productMaterial) {
		String productModelName = productModel == null ? "" : productModel.getName();
		String productSizeName = productSize == null ? "" : productSize.getName();
		String productMaterialName = productMaterial == null ? "" : productMaterial.getName();
		return productDao.getProductsForModel(productModelName, productSizeName, productMaterialName);
	}

	public Long getProductCountForModel(ProductModelEntity productModel, ProductSizeEntity productSize, ProductMaterialEntity productMaterial) {
		String productModelName = productModel == null ? "" : productModel.getName();
		String productSizeName = productSize == null ? "" : productSize.getName();
		String productMaterialName = productMaterial == null ? "" : productMaterial.getName();
		return productDao.getProductCountForModel(productModelName, productSizeName, productMaterialName);
	}

	public void removeProductFromModel(ProductEntity product) {
		try {
			if(product != null) {
				product.setProductModel(null);
				product.setProductSize(null);
				product.setProductMaterial(null);
				productDao.update(product);
			}
			msg("Product successfully updated");
		} catch (Exception e) {
			e.printStackTrace();
			msg("Error occurred!");
		}
	}

	
	
	
	//--------------------------------- Getters and Setters ------------------------------------------------------------
	
	
	
	public List<TermsEntity> getAllTerms() {
		setAllTerms(termsDao.getAllTerms());
		return allTerms;
	}

	public void setAllTerms(List<TermsEntity> allTerms) {
		this.allTerms = allTerms;
	}

	public TermsDao getTermsDao() {
		return termsDao;
	}

	public void setTermsDao(TermsDao termsDao) {
		this.termsDao = termsDao;
	}

	public List<ProductEntity> getProducts() {
		setProducts(productDao.getAllProducts());
		return products;
	}

	public void setProducts(List<ProductEntity> products) {
		this.products = products;
	}

	public CompanyDao getCompanyDao() {
		return companyDao;
	}

	public void setCompanyDao(CompanyDao companyDao) {
		this.companyDao = companyDao;
	}

	public ProductDao getProductDao() {
		return productDao;
	}

	public void setProductDao(ProductDao productDao) {
		this.productDao = productDao;
	}

	public ProductEntity getProduct() {
		return product;
	}

	public ProductModelDao getProductModelDao() {
		return productModelDao;
	}

	public void setProductModelDao(ProductModelDao productModelDao) {
		this.productModelDao = productModelDao;
	}

	public ProductSizeDao getProductSizeDao() {
		return productSizeDao;
	}

	public void setProductSizeDao(ProductSizeDao productSizeDao) {
		this.productSizeDao = productSizeDao;
	}

	public void setProduct(ProductEntity product) {
		this.product = product;
	}

	public UserService getUserService() {
		return userService;
	}

	public void setUserService(UserService userService) {
		this.userService = userService;
	}

	public List<UserEntity> getUsers() {
		setUsers(userService.getAllUsers());
		return users;
	}

	public void setUsers(List<UserEntity> users) {
		this.users = users;
	}

	public UserEntity getUser() {
		return user;
	}

	public void setUser(UserEntity user) {
		this.user = user;
	}

	public Integer getActiveIndex() {
		return activeIndex;
	}

	public void setActiveIndex(Integer activeIndex) {
		this.activeIndex = activeIndex % 7;
        if(activeIndex == 0)
            redirectTo("dashboard");
		if(activeIndex == 1)
			redirectTo("offers");
        if(activeIndex == 2)
            redirectTo("orders");
		if(activeIndex == 3)
            redirectTo("invoices");
        if(activeIndex == 4)
			redirectTo("administration");
		if(activeIndex == 5)
			redirectTo("statistics");
        if(activeIndex == 6)
            redirectTo("stock");
	}

	public TermsEntity getTerms() {
		return terms;
	}

	public void setTerms(TermsEntity terms) {
		this.terms = terms;
	}

	public List<CompanyEntity> getCompanies() {
		setCompanies(companyDao.getCompanyList());
		Collections.sort(companies,new Comparator<CompanyEntity>() {
			public int compare(CompanyEntity x1, CompanyEntity x2) {
				if(x2 == null || x1 == null)
					return 0;
				return x1.getName().compareTo(x2.getName());
			}
		});
		return companies;
	}

	public void setCompanies(List<CompanyEntity> companies) {
		this.companies = companies;
	}

	public CompanyEntity getCompany() {
		return company;
	}

	public void setCompany(CompanyEntity company) {
		this.company = company;
	}

	public CountryDao getCountryDao() {
		return countryDao;
	}

	public void setCountryDao(CountryDao countryDao) {
		this.countryDao = countryDao;
	}

	public boolean isEdit() {
		return edit;
	}

	public void setEdit(boolean edit) {
		this.edit = edit;
	}

	public List<CountryEntity> getCountries() {
		setCountries(countryDao.getCountries());
		return countries;
	}

	public void setCountries(List<CountryEntity> countries) {
		this.countries = countries;
	}

	public CountryEntity getCountry() {
		return country;
	}

	public void setCountry(CountryEntity country) {
		this.country = country;
	}

	public List<PersonEntity> getPersons() {
		setPersons(personDao.getPersons());
		return persons;
	}

	public void setPersons(List<PersonEntity> persons) {
		this.persons = persons;
	}

	public PersonDao getPersonDao() {
		return personDao;
	}

	public void setPersonDao(PersonDao personDao) {
		this.personDao = personDao;
	}

	public PersonEntity getPerson() {
		return person;
	}

	public void setPerson(PersonEntity person) {
		this.person = person;
	}

    public CategoryDao getCategoryDao() {
        return categoryDao;
    }

    public void setCategoryDao(CategoryDao categoryDao) {
        this.categoryDao = categoryDao;
    }

    public List<CategoryEntity> getCategories() {
        return categories;
    }

    public void setCategories(List<CategoryEntity> categories) {
        this.categories = categories;
    }

    public CategoryEntity getCategory() {
        return category;
    }

    public void setCategory(CategoryEntity category) {
        this.category = category;
    }

    public List<ProductEntity> getCategoryProducts() {
        return categoryProducts;
    }

    public void setCategoryProducts(List<ProductEntity> categoryProducts) {
        this.categoryProducts = categoryProducts;
    }

    public ProductEntity[] getSelectedProducts() {
        return selectedProducts;
    }

    public void setSelectedProducts(ProductEntity[] selectedProducts) {
        this.selectedProducts = selectedProducts;
    }

    public ProductModel getProductModel() {
        List<ProductEntity> products = new ArrayList<ProductEntity>();
        for(ProductEntity p: getProducts()) {
            if(p.getCategory() == null)
                products.add(p);
        }
        productModel = new ProductModel(products);
        return productModel;
    }

    public void setProductModel(ProductModel productModel) {
        this.productModel = productModel;
    }

    public List<CategoryEntity> getSelectedCategories() {
        return selectedCategories;
    }

    public void setSelectedCategories(List<CategoryEntity> selectedCategories) {
        this.selectedCategories = selectedCategories;
    }

    public CategoryEntity getSelectedCategory() {
        return selectedCategory;
    }

    public void setSelectedCategory(CategoryEntity selectedCategory) {
        this.selectedCategory = selectedCategory;
    }

    public CategoryEntity getSelectedSubCategory() {
        return selectedSubCategory;
    }

    public void setSelectedSubCategory(CategoryEntity selectedSubCategory) {
        this.selectedSubCategory = selectedSubCategory;
    }

    public CategoryModel getCategoryModel() {
        return categoryModel;
    }

    public void setCategoryModel(CategoryModel categoryModel) {
        this.categoryModel = categoryModel;
    }

    public UploadedFile getCategoryImage() {
        return categoryImage;
    }

    public void setCategoryImage(UploadedFile categoryImage) {
        this.categoryImage = categoryImage;
    }

    public CategoryEntity getSelectedSubCategory2() {
        return selectedSubCategory2;
    }

    public void setSelectedSubCategory2(CategoryEntity selectedSubCategory2) {
        this.selectedSubCategory2 = selectedSubCategory2;
    }

    public void safeSetSelectedSubCategory2(CategoryEntity selectedSubCategory2) {
        if(selectedSubCategory2 != null)
            this.selectedSubCategory2 = categoryDao.getCategory(selectedSubCategory2.getName());
        else
            this.selectedSubCategory2 = selectedSubCategory2;
    }

	public List<ProductModelEntity> getProductModels() {
		return productModelDao.getAllProductModels();
	}

	public List<ProductSizeEntity> getProductSizes() {
		return productSizeDao.getAllProductSizes();
	}

	public List<ProductMaterialEntity> getProductMaterials() {
		return productMaterialDao.getAllProductMaterials();
	}

	public ProductModelEntity getSelectedProductModel() {
		return selectedProductModel;
	}

	public void setSelectedProductModel(ProductModelEntity selectedProductModel) {
		this.selectedProductModel = selectedProductModel;
	}

	public ProductSizeEntity getSelectedProductSize() {
		return selectedProductSize;
	}

	public void setSelectedProductSize(ProductSizeEntity selectedProductSize) {
		this.selectedProductSize = selectedProductSize;
	}

	public ProductModelEntity getProductModelEntity() {
		return productModelEntity;
	}

	public void setProductModelEntity(ProductModelEntity productModelEntity) {
		this.productModelEntity = productModelEntity;
	}

	public ProductSizeEntity getProductSize() {
		return productSize;
	}

	public void setProductSize(ProductSizeEntity productSize) {
		this.productSize = productSize;
	}

	public ProductMaterialEntity getProductMaterial() {
		return productMaterial;
	}

	public void setProductMaterial(ProductMaterialEntity productMaterial) {
		this.productMaterial = productMaterial;
	}

	public ProductMaterialEntity getSelectedProductMaterial() {
		return selectedProductMaterial;
	}

	public void setSelectedProductMaterial(ProductMaterialEntity selectedProductMaterial) {
		this.selectedProductMaterial = selectedProductMaterial;
	}

	public ProductMaterialDao getProductMaterialDao() {
		return productMaterialDao;
	}

	public void setProductMaterialDao(ProductMaterialDao productMaterialDao) {
		this.productMaterialDao = productMaterialDao;
	}

	public ProductModel getProductModel1() {
		List<ProductEntity> products = new ArrayList<ProductEntity>();
		for(ProductEntity p: getProducts()) {
			if(p.getProductModel() == null)
				products.add(p);
		}
		productModel = new ProductModel(products);
		return productModel;
	}

	public void setProductModel1(ProductModel productModel1) {
		this.productModel1 = productModel1;
	}
}
