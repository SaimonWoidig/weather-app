import "./WeatherForm.css"

import {type Component} from "solid-js";
import type {SetStoreFunction} from "solid-js/store";
import {createForm, custom, FormError, required, type SubmitHandler} from "@modular-forms/solid";

import type {TWeather} from "@lib/types/weather.ts";
import type {TGeo} from "@lib/types/geo.ts";

type WeatherFormProps = {
    defaultLocation: TGeo
    setWeatherData: SetStoreFunction<TWeather>
}

const WeatherForm: Component<WeatherFormProps> = (formProps) => {
    const [weatherForm, {Form, Field}] = createForm<TGeo>();
    const handleSubmit: SubmitHandler<TGeo> = async (values) => {
        const res = await fetch(
            `/api/weather?latitude=${values.latitude}&longitude=${values.longitude}&type=current`,
            {method: "GET"}
        );
        if (!res.ok) {
            throw new FormError<TGeo>(res.statusText);
        }
        const data: TWeather = await res.json();
        formProps.setWeatherData(data);
    };
    return (
        <Form onSubmit={handleSubmit} name="weather" class="weather-form">
            <div>
                <Field name="latitude" type="number" validate={[
                    required("Latitude is required"),
                    custom(
                        (value) => (value !== undefined && value >= -90 && value <= 90),
                        "Latitude must be between -90 and 90"
                    )
                ]}>
                    {(field, props) => (
                        <>
                            <label for="latitude">Latitude:</label>
                            <input {...props} type="number" required
                                   value={field.value || formProps.defaultLocation.latitude}/>
                            {field.error && <div class="field-error">{field.error}</div>}
                        </>
                    )}
                </Field>
            </div>
            <div>
                <Field name="longitude" type="number" validate={[
                    required("Longitude is required"),
                    custom(
                        (value) => (value !== undefined && value >= -180 && value <= 180),
                        "Longitude must be between -180 and 180")
                ]}>
                    {(field, props) => (
                        <>
                            <label for="longitude">Longitude:</label>
                            <input {...props} type="number" required
                                   value={field.value || formProps.defaultLocation.longitude}/>
                            {field.error && <div class="field-error">{field.error}</div>}
                        </>
                    )}
                </Field>
            </div>
            <div>
                <button type="submit" disabled={weatherForm.submitting || weatherForm.invalid}>Submit</button>
            </div>
            {weatherForm.response.status === "error" && <div class="field-error">{weatherForm.response.message}</div>}
        </Form>
    )
}

export default WeatherForm;