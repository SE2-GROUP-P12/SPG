import 'bootstrap/dist/css/bootstrap.min.css';
import './App.css';
import Button from "react-bootstrap/Button"
import Grid from '@mui/material/Grid';
import {Redirect, Link} from "react-router-dom";
import {FastField, Field, Form, Formik} from "formik";
import * as Yup from "yup";
import {getSalt} from "./Utilities";
import {API} from "./API/API";
import Alert from "react-bootstrap/Alert";
import {Dropdown} from "react-bootstrap";
import DropdownItem from "react-bootstrap/DropdownItem";
import DropdownMenu from "react-bootstrap/DropdownMenu";
import DropdownToggle from "react-bootstrap/DropdownToggle";

const possibleUnits = ['Kg', 'gr', 'L', 'ml', 'units']

function AddProduct() {
    return (
        localStorage.getItem('role') === 'FARMER' ?
            <>
                <h1>Add New Product</h1>
                <Formik
                    initialValues={{
                        productName: '',
                        price: '',
                        unitOfMeasurement: '',
                        imageUrl: ''
                    }}
                    validationSchema={
                        Yup.object({
                            productName: Yup.string().required('Product Name is required').max(50, "Name can't be longer than 50 characters"),
                            price: Yup.string().required('Price is required').matches(/^(?=.*?\d)^(([1-9]\d{0,2}(,\d{3})*)|\d+)?(\.\d{1,2})?$/, 'Please insert a valid price'),
                            unitOfMeasurement: Yup.string().required('A Unit of measurement is required'),
                            imageUrl: Yup.string().matches(/^(http)?s?:?(\/\/[^"']*\.(?:png|jpg|jpeg))$|^$/, 'Insert a valid URL containing a png, jpg or jpeg image')
                        })
                    }
                    onSubmit={async (values) => {
                        let requestBodyObject = {
                            email: localStorage.getItem("username"),
                            productName: values.productName,
                            price: values.price,
                            unitOfMeasurement: values.unitOfMeasurement,
                            imageUrl: values.imageUrl
                        };
                        alert(JSON.stringify(requestBodyObject, null,2))
                        let response = await API.addProduct(requestBodyObject);
                        if (response) {
                            console.log("everything ok")
                        } else {
                            console.log("something bad happened")
                        }
                    }}
                    validateOnBlur={false}
                    validateOnChange={false}
                >
                    {({values, errors, touched, setFieldValue}) =>
                        <Form>
                            {errors.productName && touched.productName ?
                                <Alert variant='danger'>{errors.productName}</Alert> : null}
                            {errors.price && touched.price ? <Alert variant='danger'>{errors.price}</Alert> : null}
                            {errors.unitOfMeasurement && touched.unitOfMeasurement ?
                                <Alert variant='danger'>{errors.unitOfMeasurement}</Alert> : null}
                            {errors.imageUrl && touched.imageUrl ?
                                <Alert variant='danger'>{errors.imageUrl}</Alert> : null}
                            <Grid container align='center'>
                                {/*PRODUCT NAME FIELD*/}
                                <Grid item xs={1} sm={2}/>
                                <Grid item xs={4} sm={2} align='left'><label htmlFor="productName">Product Name*</label></Grid>
                                <Grid item xs={6}><FastField id='productName' type="text" name="productName"
                                                             label="Product Name"
                                                             style={{width: '100%'}}/></Grid>
                                <Grid item xs={1} sm={2}/>
                                {/*PRICE FIELD*/}
                                <Grid item xs={1} sm={2}/>
                                <Grid item xs={4} sm={2} align='left'><label htmlFor='price'>Price*</label></Grid>
                                <Grid item xs={6}><FastField id='price' type="number" name="price" label="Price" min="0"
                                                             step="0.01"
                                                             style={{width: '100%'}}/></Grid>
                                <Grid item xs={1} sm={2}/>
                                {/*UNIT OF MEASUREMENT FIELD*/}
                                <Grid item xs={1} sm={2}/>
                                <Grid item xs={4} sm={2} align='left'><label htmlFor='unitOfMeasurement'>Unit Of
                                    Measurement*</label></Grid>
                                <Grid item xs={6}>
                                    <Dropdown onSelect={(val, x) => setFieldValue('unitOfMeasurement', val)}>
                                        <DropdownToggle id="dropdown-measure" variant="success">
                                            Select a Unit Of Measurement
                                        </DropdownToggle>
                                        <DropdownMenu>
                                            {possibleUnits.map(u =>
                                                <DropdownItem eventKey={u}>{u}</DropdownItem>)}
                                        </DropdownMenu>
                                    </Dropdown>
                                </Grid>

                                <Grid item xs={1} sm={2}/>
                                {/*IMAGE URL FIELD*/}
                                <Grid item xs={1} sm={2}/>
                                <Grid item xs={4} sm={2} align='left'><label htmlFor='imageUrl'>Image Url</label></Grid>
                                <Grid item xs={6}><FastField id='imageUrl' type="text" name="imageUrl" label="imageUrl"
                                                             style={{width: '100%'}}/></Grid>
                                <Grid item xs={1} sm={2}/>
                                {/*BUTTONs COMPONENT*/}
                                <Grid item xs={6}>
                                    <Link to="/ShopEmployee"><Button variant="secondary" size="lg"
                                                                     className="mt-4">Back</Button></Link>
                                </Grid>
                                <Grid item xs={6}>
                                    <Button type='Submit' variant="success" size="lg" className="mt-4">Submit</Button>
                                </Grid>
                            </Grid>
                            (
                            <div>
                                <pre>{JSON.stringify(values, null, 2)}</pre>
                            </div>
                            );
                        </Form>

                    }
                </Formik>
            </>
            :
            <Redirect to="/ErrorHandler"></Redirect>
    );
}

export {AddProduct}