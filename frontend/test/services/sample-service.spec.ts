import {expect, test} from 'vitest';
import {sample} from "../../src/lib/services/weather-api/sample-service.ts";

test("sample service test", () => {
    const input = 2

    const expected = 2

    const actual = sample(input)

    expect(expected).to.eq(actual)
})