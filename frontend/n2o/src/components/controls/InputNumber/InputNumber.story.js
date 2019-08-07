import React from 'react';
import { storiesOf } from '@storybook/react';
import { jsxDecorator } from 'storybook-addon-jsx';
import { withKnobs, text, boolean, number } from '@storybook/addon-knobs/react';
import withTests from 'N2oStorybook/withTests';
import withForm from 'N2oStorybook/decorators/withForm';

import InputNumber from './InputNumber';
import InputNumberJson from './InputNumber.meta.json';

const stories = storiesOf('Контролы/Ввод чисел', module);

stories.addDecorator(withKnobs);
stories.addDecorator(withTests('InputNumber'));
stories.addDecorator(jsxDecorator);

const form = withForm({ src: 'InputNumber' });

stories
  .add('Компонент', () => {
    const props = {
      visible: true,
      step: '0.1',
      showButtons: true,
    };

    return <InputNumber {...props} />;
  })

  .add(
    'Метаданные',
    form(() => {
      const props = {
        visible: boolean('visible', InputNumberJson.visible),
        step: text('step', InputNumberJson.step),
        showButtons: boolean('showButtons', InputNumberJson.showButtons),
        disabled: boolean('disabled', InputNumberJson.disabled),
        min: number('min', InputNumberJson.min),
        max: number('max', InputNumberJson.max),
      };
      return props;
    })
  )

  .add('Min/max', () => {
    const props = {
      value: 1,
      min: -150,
      max: 150,
    };

    return <InputNumber {...props} />;
  })

  .add('Шаги', () => {
    const props = {
      value: 1,
      min: -150,
      max: 150,
    };

    return (
      <React.Fragment>
        <div className="row" style={{ marginBottom: '10px' }}>
          <InputNumber {...props} step="10" />
        </div>
        <div className="row">
          <InputNumber {...props} step="0.05" />
        </div>
      </React.Fragment>
    );
  })

  .add('Свойство precision', () => {
    const props = {
      value: 1,
      step: 1,
      min: -150,
      max: 150,
      showButtons: false,
    };

    return (
      <React.Fragment>
        <div className="row mb-1 flex-column">
          <div className="row">Без дробной части</div>
          <div className="row">
            <InputNumber {...props} precision={0} />
          </div>
        </div>
        <div className="row mb-1 flex-column">
          <div className="row">Только 2 символа после запятой</div>
          <div className="row">
            <InputNumber {...props} precision={2} />
          </div>
        </div>
      </React.Fragment>
    );
  })

  .add('Без кнопок', () => {
    const props = {
      value: 1,
      min: -150,
      max: 150,
      showButtons: false,
    };

    return <InputNumber {...props} />;
  });
