import React, { Fragment } from 'react';
import { storiesOf } from '@storybook/react';
import { withKnobs, text, boolean, select } from '@storybook/addon-knobs/react';
import { map } from 'lodash';

import Title from './Title';
import meta from './Title.meta';
import { allColors } from '../utils';

const stories = storiesOf('UI Компоненты/Типография/Title', module);

stories.addDecorator(withKnobs);
stories
  .add('Компонент', () => {
    const props = {
      code: boolean('code', meta.code),
      del: boolean('del', meta.del),
      mark: boolean('mark', meta.mark),
      strong: boolean('strong', meta.strong),
      underline: boolean('underline', meta.underline),
      small: boolean('small', meta.small),
      level: select('level', [1, 2, 3, 4, 5], meta.level),
      value: text('value', meta.value),
      color: select('color', allColors, meta.color),
    };

    return <Title {...props} />;
  })
  .add('Разное отображение', () => (
    <Fragment>
      <h1>Level:</h1>
      <code>{`{level:1,2,3,4,5}`}</code>
      <Title value={meta.value} level={1} />
      <Title value={meta.value} level={2} />
      <Title value={meta.value} level={3} />
      <Title value={meta.value} level={4} />
      <Title value={meta.value} level={5} />
      <br />
      <h1>Transform:</h1>
      <code>{`{code:true}`}</code>
      <Title value={meta.value} level={3} code={true} />
      <code>{`{del:true}`}</code>
      <Title value={meta.value} level={3} del={true} />
      <code>{`{mark:true}`}</code>
      <Title value={meta.value} level={3} mark={true} />
      <code>{`{strong:true}`}</code>
      <Title value={meta.value} level={3} strong={true} />
      <code>{`{underline:true}`}</code>
      <Title value={meta.value} level={3} underline={true} />
      <code>{`{small:true}`}</code>
      <Title value={meta.value} level={3} small={true} />
      <br />
      <h1>Colors:</h1>
      {map(allColors, color => (
        <Title color={color} level={2} value={meta.value} />
      ))}
    </Fragment>
  ));
