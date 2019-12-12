import React from 'react';
import { storiesOf } from '@storybook/react';
import Placeholder from '../../Placeholder';
import meta from './List.meta';
import List from './List';

const stories = storiesOf('UI Компоненты/Placeholder/type=list', module);

stories.addParameters({
  info: {
    propTables: [List],
  },
});

stories
  .add(
    'Компонент',
    () => {
      const props = {
        loading: true,
        type: meta.type,
        rows: meta.rows,
        paragraphs: meta.paragraphs,
        avatar: meta.avatar,
      };

      return <Placeholder {...props} />;
    },
    {
      info: {
        text: `
    Компонент 'Placeholder' списка
    ~~~js
    import Placeholder from 'n2o-framework/lib/components/snippets/Placeholder/Placeholder';
    
    <Placeholder
        loading={true}
        type="list"
        rows={1}
        paragraphs={2}
     />
    ~~~
    `,
      },
    }
  )
  .add(
    'Аватар',
    () => {
      const props = {
        loading: true,
        avatar: true,
        row: 1,
        paragraph: 6,
      };

      return <Placeholder {...props} />;
    },
    {
      info: {
        text: `
    Компонент 'Placeholder' списка
    ~~~js
    import Placeholder from 'n2o-framework/lib/components/snippets/Placeholder/Placeholder';
    
    <Placeholder
        loading={true}
        type="list"
        rows={1}
        paragraphs={2}
        avatar={true}
     />
    ~~~
    `,
      },
    }
  );
