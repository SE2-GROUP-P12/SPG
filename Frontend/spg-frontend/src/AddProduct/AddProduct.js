import 'bootstrap/dist/css/bootstrap.min.css';
import '../App.css';
import Button from "react-bootstrap/Button"
import Grid from '@mui/material/Grid';
import {Redirect, Link} from "react-router-dom";
import {FastField, Form, Field, Formik} from "formik";
import * as Yup from "yup";
import {API} from "../API/API";
import Alert from "react-bootstrap/Alert";
import {useState} from 'react';

const possibleUnits = ['Kg', 'gr', 'L', 'ml', 'units']

function AddProduct() {

    const [success, setSuccess] = useState(false);
    const [error, setError] = useState(false);

    return (
        localStorage.getItem('role') === 'FARMER' ?
            <>
                <h1>Add New Product</h1>
                <Formik
                    initialValues={{
                        productName: '',
                        price: '',
                        unitOfMeasurement: 'Unit of Measurement',
                        imageUrl: ''
                    }}
                    validationSchema={
                        Yup.object({
                            productName: Yup.string().required('Product Name is required').max(50, "Name can't be longer than 50 characters"),
                            price: Yup.string().required('Price is required').matches(/^(?=.*?\d)^(([1-9]\d{0,2}(,\d{3})*)|\d+)?(\.\d{1,2})?$/, 'Please insert a valid price'),
                            unitOfMeasurement: Yup.string().required('A Unit of measurement is required').notOneOf(["Unit of Measurement"], 'A Unit of measurement is required'),
                            imageUrl: Yup.string().matches(/^(http)?s?:?(\/\/[^"']*\.(?:.*))$|^$/, 'Insert a valid URL')
                        })
                    }
                    onSubmit={async (values, {resetForm}) => {
                        setError(false);
                        setSuccess(false);
                        let requestBodyObject = {
                            email: localStorage.getItem("username"),
                            productName: values.productName,
                            price: values.price,
                            unitOfMeasurement: values.unitOfMeasurement,
                            imageUrl: values.imageUrl
                        };
                        //alert(JSON.stringify(requestBodyObject, null,2))
                        let response = await API.addProduct(requestBodyObject);
                        if (response) {
                            setSuccess(true);
                            resetForm();
                        } else {
                            setError(true);
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
                            {success ? <Alert variant='success'>Product successfully added</Alert> : ""}
                            {error ? <Alert variant = 'danger'>Something went wrong </Alert> : ""}
                            <Grid container align='center' spacing={2}>
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
                                <Grid item xs={4} sm={2} align='left'><label htmlFor='unitOfMeasurement'>Unit Of Measurement*</label></Grid>
                                <Grid item xs={6}>
                                    <Field as="select" name="unitOfMeasurement" id='unitOfMeasurement'>
                                        <option selected disabled hidden value="Unit of Measurement" >Unit of Measurement</option>
                                        {possibleUnits.map(u => <option value={u}>{u}</option>)}
                                    </Field>
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
                                    <Link to={'/Dashboard'}><Button variant="secondary" size="lg"
                                                                     className="mt-4">Back</Button></Link>
                                </Grid>
                                <Grid item xs={6}>
                                    <Button type='Submit' variant="success" size="lg" className="mt-4">Submit</Button>
                                </Grid>
                            </Grid>
                        </Form>

                    }
                </Formik>
            </>
            :
            <Redirect to="/ErrorHandler"></Redirect>
    );
}

export {AddProduct}