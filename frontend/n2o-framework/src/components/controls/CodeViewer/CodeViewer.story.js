import React from 'react';
import { storiesOf } from '@storybook/react';
import CodeViewer from './CodeViewer';

const stories = storiesOf('Контролы/Просмотрщик кода', module);

stories.add(
  'Компонент',
  () => {
    const props = {
      theme: 'light',
      visible: true,
      language: 'javascript',
      showLineNumbers: true,
      children: `function makeWorker() {
  let name = "Pete";

  return function() {
    alert(name);
  };
}`,
    };

    return <CodeViewer {...props} />;
  },
  {
    info: {
      text: `
      Компонент 'Просмотрщик кода'
      ~~~js
      import CodeViewer from 'n2o-framework/lib/components/controls/CodeViewer/CodeViewer';
      
      <CodeViewer 
        visible={true}
        language="javascript"
        theme="light"
        showLineNumbers={true}
      >
        {codeString}
      </CodeViewer>
      ~~~
      `,
    },
  }
);
