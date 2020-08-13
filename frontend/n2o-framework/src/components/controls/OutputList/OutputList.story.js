import React from 'react';

import omit from 'lodash/omit';

import { storiesOf } from '@storybook/react';

import OutputList from './OutputList';
import OutputListJson from './OutputList.meta.json';

const stories = storiesOf('Контролы/OutputList', module);

const data = [
  {
    name: 'Test link 1',
    href: 'http://...',
  },
  {
    name: 'Test link 2',
    href: 'http://...',
  },
  {
    name: 'Test link 3',
    href: 'http://...',
  },
  {
    name: 'Test link 5',
    href: 'http://...',
  },
  {
    name: 'Test link 6',
    href: 'http://...',
  },
  {
    name: 'Test link 7',
    href: 'http://...',
  },
];

const data2 = [
  {
    name: 'Test row 1',
  },
  {
    name: 'Test row 2',
  },
  {
    name: 'Test row 3',
  },
  {
    name: 'Test row 5',
  },
  {
    name: 'Test row 6',
  },
  {
    name: 'Test row 7',
  },
];

stories
  .add(
    'OutputList row links',
    () => {
      return <OutputList {...OutputListJson} data={data} />;
    },
    {
      info: {
        text: `
      Компонент 'Вывод строк/список'
      ~~~js
      import OutputList from 'n2o-framework/lib/components/controls/OutputList/OutputList';
      
      <OutputList
          className="custom-class"
          labelFieldId="name"
          linkFieldId="href"
          target="application"
          direction="row"
          separator=","
      />
      ~~~
      `,
      },
    }
  )
  .add(
    'OutputList row',
    () => {
      return <OutputList {...OutputListJson} data={data2} />;
    },
    {
      info: {
        text: `
      Компонент 'Вывод строк/список'
      ~~~js
      import OutputList from 'n2o-framework/lib/components/controls/OutputList/OutputList';
      
      <OutputList
          className="custom-class"
          labelFieldId="name"
          linkFieldId="href"
          target="application"
          direction="row"
          separator=","
      />
      ~~~
      `,
      },
    }
  )
  .add(
    'without separator',
    () => {
      return <OutputList {...omit(OutputListJson, 'separator')} data={data2} />;
    },
    {
      info: {
        text: `
      Компонент 'Вывод строк/список'
      ~~~js
      import OutputList from 'n2o-framework/lib/components/controls/OutputList/OutputList';
      
      <OutputList
          className="custom-class"
          labelFieldId="name"
          linkFieldId="href"
          target="application"
          direction="row"
      />
      ~~~
      `,
      },
    }
  )
  .add(
    'OutputList column links',
    () => {
      return (
        <OutputList {...OutputListJson} data={data} direction={'column'} />
      );
    },
    {
      info: {
        text: `
      Компонент 'Вывод строк/список'
      ~~~js
      import OutputList from 'n2o-framework/lib/components/controls/OutputList/OutputList';
      
      <OutputList
          className="custom-class"
          labelFieldId="name"
          linkFieldId="href"
          target="application"
          direction="column"
          separator=","
      />
      ~~~
      `,
      },
    }
  )
  .add(
    'OutputList column',
    () => {
      return (
        <OutputList {...OutputListJson} data={data2} direction={'column'} />
      );
    },
    {
      info: {
        text: `
      Компонент 'Вывод строк/список'
      ~~~js
      import OutputList from 'n2o-framework/lib/components/controls/OutputList/OutputList';
      
      <OutputList
          className="custom-class"
          labelFieldId="name"
          linkFieldId="href"
          target="application"
          direction="column"
          separator=","
      />
      ~~~
      `,
      },
    }
  );
