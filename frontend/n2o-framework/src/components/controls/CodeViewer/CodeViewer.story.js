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
      visible: true,
      language: 'javascript',
      showLineNumbers: true,
      value: `function makeWorker() {
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
        showLineNumbers={true}
        hideButtons={false}
        startingLineNumber={1}
        hideOverflow={false}
      >
        {value}
      </CodeViewer>
      ~~~
      `,
    },
  }
);
