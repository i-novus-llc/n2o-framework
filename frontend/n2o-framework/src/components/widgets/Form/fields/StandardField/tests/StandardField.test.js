import React from 'react'
import TestRenderer from 'react-test-renderer'

import StandardField from '../StandardField'

it('StandardField отрисовывается', () => {
    const testRender = TestRenderer.create(
        <StandardField
            id="myField"
            value="test"
            visible
            label="Мое поле"
            control="InputText"
            description="Введите значение"
            measure="км"
            required
            className="test"
            style={{ display: 'block' }}
            validationState="error"
            loading={false}
            disabled={false}
            enabled
            labelStyle={{ display: 'block' }}
            controlStyle={{ display: 'block' }}
            labelClass="myLabelClass"
            controlClass="myControlClass"
            placeholder="TEST"
            component={() => null}
            toolbar={[{buttons: []}]}
        />,
    ).root

    expect(testRender).toBeTruthy()
})
