import React, { Fragment } from 'react';
import { storiesOf } from '@storybook/react';

import Base from '../Base';
import Paragraph from './Paragraph';
import meta from './Paragraph.meta';
import { allColors } from '../utils';
import map from 'lodash/map';

const stories = storiesOf('UI Компоненты/Типография/Paragraph', module);

stories.addParameters({
  info: {
    propTables: [Base],
    propTablesExclude: [Paragraph],
  },
});

const txt =
  'Mr. Dursley was the director of a firm called Grunnings, which made drills. He was a big, beefy man with hardly any neck, although he did have a very large mustache. Mrs. Dursley was thin and blonde and had nearly twice the usual amount of neck, which came in very useful as she spent so much of her time craning over garden fences, spying on the neighbors. The Dursleys had a small son called Dudley and in their opinion there was no finer boy anywhere.';

stories
  .add(
    'Компонент',
    () => {
      const props = {
        code: meta.code,
        del: meta.del,
        mark: meta.mark,
        strong: meta.strong,
        underline: meta.underline,
        small: meta.small,
        copyable: meta.copyable,
        text: meta.text,
        editable: meta.editable,
      };

      return <Paragraph {...props} />;
    },
    {
      info: {
        text: `
      Компонент 'Параграф'
      ~~~js
      import Paragraph from 'n2o-framework/lib/components/snippets/Typography/Paragraph/Paragraph';
      
      <Paragraph
          text="Paragraph"
       />
      ~~~
      `,
      },
    }
  )
  .add(
    'Разное отображение',
    () => (
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
    ),
    {
      info: {
        text: `
      Компонент 'Параграф'
      ~~~js
      import Paragraph from 'n2o-framework/lib/components/snippets/Typography/Paragraph/Paragraph';
      
      <Fragment>
        <br />
        <h1>Transform:</h1>
        <code>{\`{code:true}\`}</code>
        <Paragraph text={txt} code={true} />
        <code>{\`{del:true}\`}</code>
        <Paragraph text={txt} del={true} />
        <code>{\`{mark:true}\`}</code>
        <Paragraph text={txt} mark={true} />
        <code>{\`{strong:true}\`}</code>
        <Paragraph text={txt} strong={true} />
        <code>{\`{underline:true}\`}</code>
        <Paragraph text={txt} underline={true} />
        <code>{\`{small:true}\`}</code>
        <Paragraph text={txt} small={true} />
        <code>{\`{copyable:true}\`}</code>
        <Paragraph text={txt} copyable={true} />
        <code>{\`{editable:true}\`}</code>
        <Paragraph text={txt} editable={true} />
        <br />
        <h1>Colors:</h1>
        {map(allColors, color => (
          <Paragraph color={color} text={txt} />
        ))}
      </Fragment>
      ~~~
      `,
      },
    }
  );
