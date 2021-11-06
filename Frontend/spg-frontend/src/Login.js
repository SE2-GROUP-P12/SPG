import 'bootstrap/dist/css/bootstrap.min.css';
import './App.css';
import { useFormik } from 'formik';
import * as Yup from 'yup';

function Login() {
    const formik = useFormik({
        initialValues: {
            email: '',
            password: ''
        },
        validationSchema: Yup.object({
            email: Yup.string().email('Invalid email address').required('Email is required'),
            password: Yup.string().required('Password is required').min(6, "Password is too short")
        }),
        onSubmit: values => {
            alert(JSON.stringify(values, null, 2));
        },
    });

    return (
        <div><h1>Login</h1><form>
        <label>Email Address</label>
        <input
            id="email"
            name="email"
            type="email"
            onChange={formik.handleChange}
            onBlur={formik.handleBlur}
            value={formik.values.email}
        />
        {formik.touched.email && formik.errors.email ? (
            <div>{formik.errors.email}</div>
        ) : null}
        <label>Password</label>
            <input
            id="password"
            name="password"
            type="password"
            onChange={formik.handleChange}
            onBlur={formik.handleBlur}
            value={formik.values.password}
            />
            {formik.touched.password && formik.errors.password ? (
                <div>{formik.errors.password}</div>
            ) : null}
        <button type="submit">Submit</button>
    </form></div>
    );
}

export {Login}