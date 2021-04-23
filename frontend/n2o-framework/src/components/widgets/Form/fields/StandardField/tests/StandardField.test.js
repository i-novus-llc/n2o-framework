import React from 'react'
import TestRenderer from 'react-test-renderer'

import StandardField from '../StandardField'

const toolbar = [
    {
        buttons: [
            {
                src: 'StandardButton',
                id: 'update',
                label: 'button 1',
                icon: 'fa fa-trash',
                actionId: 'update',
                validate: true,
                validatedWidgetId: 'create2_main',
                color: 'primary',
                hint: 'some hint',
                size: 'sm',
            },
        ],
    },
]

it.skip('StandardField рендерится корректно', () => {
    const tree = TestRenderer.create(
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
        />,
    ).toJSON()
    expect(tree).toMatchSnapshot()
})

it.skip('StandardField рендерится корректно c toolbar', () => {
    const tree = TestRenderer.create(
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
            toolbar={toolbar[0]}
        />,
    ).toJSON()
    expect(tree).toMatchSnapshot()
})

it('StandardField верно приходит props toolbar', () => {
    const tree = TestRenderer.create(
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
            toolbar={toolbar[0]}
        />,
    ).root
    expect(tree.props.toolbar).toEqual(toolbar[0])
})
