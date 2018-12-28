import React from 'react';
import TestRenderer from 'react-test-renderer';
import StandardField from '../StandardField';

it('StandardField рендерится корректно', () => {
  const tree = TestRenderer.create(
    <StandardField
      id="myField"
      value="test"
      visible={true}
      label="Мое поле"
      control="InputText"
      description="Введите значение"
      measure="км"
      required={true}
      className="test"
      style={{ display: 'block' }}
      validationState="error"
      loading={false}
      disabled={false}
      enabled={true}
      labelStyle={{ display: 'block' }}
      controlStyle={{ display: 'block' }}
      labelClass="myLabelClass"
      controlClass="myControlClass"
      placeholder="TEST"
      component={() => null}
    />
  ).toJSON();
  expect(tree).toMatchSnapshot();
});
