import React, { Fragment } from 'react';
import { storiesOf } from '@storybook/react';
import { withKnobs, text, boolean, select } from '@storybook/addon-knobs/react';

import Text from './Text';
import meta from './Text.meta';
import { allColors } from '../utils';
import { map } from 'lodash';

const stories = storiesOf('UI Компоненты/Типография/Text', module);

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

    return <Text {...props} />;
  })
  .add('Разное отображение', () => (
    <Fragment>
      <br />
      <h1>Transform:</h1>
      <code>{`{code:true}`}</code>
      <Text text="The five boxing wizards jump quickly." code={true} />
      <code>{`{del:true}`}</code>
      <Text text="The five boxing wizards jump quickly." del={true} />
      <code>{`{mark:true}`}</code>
      <Text text="The five boxing wizards jump quickly." mark={true} />
      <code>{`{strong:true}`}</code>
      <Text text="The five boxing wizards jump quickly." strong={true} />
      <code>{`{underline:true}`}</code>
      <Text text="The five boxing wizards jump quickly." underline={true} />
      <code>{`{small:true}`}</code>
      <Text text="The five boxing wizards jump quickly." small={true} />
      <code>{`{copyable:true}`}</code>
      <Text text="The five boxing wizards jump quickly." copyable={true} />
      <code>{`{editable:true}`}</code>
      <Text text="The five boxing wizards jump quickly." editable={true} />
      <br />
      <h1>Colors:</h1>
      {map(allColors, color => (
        <Text
          color={color}
          level={2}
          text="The five boxing wizards jump quickly."
        />
      ))}
    </Fragment>
  ));
