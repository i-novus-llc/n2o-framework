import React, { Fragment } from 'react';
import { storiesOf } from '@storybook/react';
import {
  withKnobs,
  boolean,
  number,
  select,
} from '@storybook/addon-knobs/react';
import Slider from './Slider';
import meta from './Slider.meta';

import withForm from 'N2oStorybook/decorators/withForm';

const stories = storiesOf('Контролы/Ползунок', module);
const form = withForm({ src: 'Slider' });
stories.addDecorator(withKnobs);

stories
  .add('Компонент', () => {
    const props = {
      multiple: boolean('multiple', meta.multiple),
      showTooltip: boolean('showTooltip', meta.showTooltip),
      tooltipPlacement: select(
        'tooltipPlacement',
        ['top', 'bottom', 'left', 'right'],
        meta.tooltipPlacement
      ),
      step: number('step', meta.step),
      vertical: boolean('vertical', meta.showTooltip),
      disabled: boolean('disabled', meta.showTooltip),
      dots: boolean('dots', meta.showTooltip),
      min: number('min', meta.min),
      max: number('min', meta.max),
      marks: {},
      pushable: boolean('pushable', meta.showTooltip),
    };

    return <Slider {...props} />;
  })
  .add('Метаданные', form(() => meta))
  .add('Тултип', () => {
    return (
      <div>
        <h4>showTooltip=true</h4>
        <Slider showTooltip={true} />
        <br />
        <h4>tooltipPlacement:</h4>
        <div>tooltipPlacement='top'</div>
        <Slider showTooltip={true} tooltipPlacement="top" />
        <div>tooltipPlacement='bottom'</div>
        <Slider showTooltip={true} tooltipPlacement="bottom" />
        <div>tooltipPlacement='left'</div>
        <Slider showTooltip={true} tooltipPlacement="left" />
        <div>tooltipPlacement='right'</div>
        <Slider showTooltip={true} tooltipPlacement="right" />
        <br />
        <h4>tooltipFormatter:</h4>
        <div>
          tooltipFormatter = <code>"`${'this'}%`"</code>
        </div>
        <Slider
          showTooltip={true}
          tooltipPlacement="top"
          tooltipFormatter={'`${this}%`'}
        />
        <div>
          tooltipFormatter = <code>"`t=${'this'}℃`"</code>
        </div>
        <Slider
          showTooltip={true}
          tooltipPlacement="top"
          tooltipFormatter={'`t=${this}℃`'}
        />
      </div>
    );
  })
  .add('Подписи на оси', () => {
    return (
      <Fragment>
        <h4>
          marks=
          <code>
            {`{
            '0': '0℃',
            '100': '100℃'
          }`}
          </code>
        </h4>
        <Slider
          marks={{ '0': '0℃', '100': '100℃' }}
          max={100}
          min={0}
          step={10}
        />
      </Fragment>
    );
  })
  .add('Мульти режим', () => {
    return (
      <Fragment>
        <h4>multiple=true</h4>
        <Slider
          multiple={true}
          min={0}
          max={100}
          defaultValue={[20, '50.5']}
          step="5"
          dots={true}
          onChange={console.log}
        />
      </Fragment>
    );
  });
