import {assert, expect, test} from 'vitest';

test('sample test', () => {
    expect(2).to.eq(2)
    assert.deepEqual({foo: "bar"}, {foo: "bar"})
});
