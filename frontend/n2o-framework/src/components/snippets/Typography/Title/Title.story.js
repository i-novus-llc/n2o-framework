import React, { Fragment } from 'react';
import { storiesOf } from '@storybook/react';

import map from 'lodash/map';

import Title from './Title';
import meta from './Title.meta';
import { allColors } from '../utils';

const stories = storiesOf('UI Компоненты/Типография/Title', module);

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
        level: meta.level,
        text: meta.text,
        color: meta.color,
        editable: meta.editable,
        copyable: meta.copyable,
      };

      return <Title {...props} />;
    },
    {
      info: {
        text: `
      Компонент 'Заголовок'
      ~~~js
      import Title from 'n2o-framework/lib/components/snippets/Typography/Title/Title';
      
      <Title text="Title" />
      ~~~
      `,
      },
    }
  )
  .add(
    'Разное отображение',
    () => (
      <Fragment>
        <h1>Level:</h1>
        <code>{`{level:1,2,3,4,5}`}</code>
        <Title text={meta.text} level={1} />
        <Title text={meta.text} level={2} />
        <Title text={meta.text} level={3} />
        <Title text={meta.text} level={4} />
        <Title text={meta.text} level={5} />
        <br />
        <h1>Transform:</h1>
        <code>{`{code:true}`}</code>
        <Title text={meta.text} level={3} code={true} />
        <code>{`{del:true}`}</code>
        <Title text={meta.text} level={3} del={true} />
        <code>{`{mark:true}`}</code>
        <Title text={meta.text} level={3} mark={true} />
        <code>{`{strong:true}`}</code>
        <Title text={meta.text} level={3} strong={true} />
        <code>{`{underline:true}`}</code>
        <Title text={meta.text} level={3} underline={true} />
        <code>{`{small:true}`}</code>
        <Title text={meta.text} level={3} small={true} />
        <br />
        <h1>Colors:</h1>
        {map(allColors, color => (
          <Title color={color} level={2} text={meta.text} />
        ))}
      </Fragment>
    ),
    {
      info: {
        text: `
      Компонент 'Заголовок'
      ~~~js
      import Title from 'n2o-framework/lib/components/snippets/Typography/Title/Title';
      
      <Fragment>
        <h1>Level:</h1>
        <code>{\`{level:1,2,3,4,5}\`}</code>
        <Title text={meta.text} level={1} />
        <Title text={meta.text} level={2} />
        <Title text={meta.text} level={3} />
        <Title text={meta.text} level={4} />
        <Title text={meta.text} level={5} />
        <br />
        <h1>Transform:</h1>
        <code>{\`{code:true}\`}</code>
        <Title text={meta.text} level={3} code={true} />
        <code>{\`{del:true}\`}</code>
        <Title text={meta.text} level={3} del={true} />
        <code>{\`{mark:true}\`}</code>
        <Title text={meta.text} level={3} mark={true} />
        <code>{\`{strong:true}\`}</code>
        <Title text={meta.text} level={3} strong={true} />
        <code>{\`{underline:true}\`}</code>
        <Title text={meta.text} level={3} underline={true} />
        <code>{\`{small:true}\`}</code>
        <Title text={meta.text} level={3} small={true} />
        <br />
        <h1>Colors:</h1>
        {map(allColors, color => (
          <Title color={color} level={2} text={meta.text} />
        ))}
      </Fragment>
      ~~~
      `,
      },
    }
  );
