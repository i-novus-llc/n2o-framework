import React from 'react';
import { storiesOf } from '@storybook/react';
import CodeViewer from './CodeViewer';
import { CodeViewer as CodeViewerComponent } from './CodeViewer';

const stories = storiesOf('Контролы/Просмотрщик кода', module);
stories.addParameters({
  info: {
    propTables: [CodeViewerComponent],
  },
});

stories.add(
  'Компонент',
  () => {
    const props = {
      title: 'makeWorker',
      text: 'Фргамент кода makeWorker',
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
        title="title"
        text="text"
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
