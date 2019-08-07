import React, { Fragment } from 'react';
import { storiesOf } from '@storybook/react';
import { jsxDecorator } from 'storybook-addon-jsx';
import { withKnobs, text, boolean, select } from '@storybook/addon-knobs/react';

import Paragraph from './Paragraph';
import meta from './Paragraph.meta';
import { allColors } from '../utils';
import { map } from 'lodash';

const stories = storiesOf('UI Компоненты/Типография/Paragraph', module);

stories.addDecorator(jsxDecorator);

const txt =
  'Mr. Dursley was the director of a firm called Grunnings, which made drills. He was a big, beefy man with hardly any neck, although he did have a very large mustache. Mrs. Dursley was thin and blonde and had nearly twice the usual amount of neck, which came in very useful as she spent so much of her time craning over garden fences, spying on the neighbors. The Dursleys had a small son called Dudley and in their opinion there was no finer boy anywhere.';

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
      copyable: boolean('copyable', meta.copyable),
      text: text('text', meta.text),
      editable: boolean('editable', meta.editable),
    };

    return <Paragraph {...props} />;
  })
  .add('Разное отображение', () => (
    <Fragment>
      <br />
      <h1>Transform:</h1>
      <code>{`{code:true}`}</code>
      <Paragraph text={txt} code={true} />
      <code>{`{del:true}`}</code>
      <Paragraph text={txt} del={true} />
      <code>{`{mark:true}`}</code>
      <Paragraph text={txt} mark={true} />
      <code>{`{strong:true}`}</code>
      <Paragraph text={txt} strong={true} />
      <code>{`{underline:true}`}</code>
      <Paragraph text={txt} underline={true} />
      <code>{`{small:true}`}</code>
      <Paragraph text={txt} small={true} />
      <code>{`{copyable:true}`}</code>
      <Paragraph text={txt} copyable={true} />
      <code>{`{editable:true}`}</code>
      <Paragraph text={txt} editable={true} />
      <br />
      <h1>Colors:</h1>
      {map(allColors, color => (
        <Paragraph color={color} text={txt} />
      ))}
    </Fragment>
  ));
