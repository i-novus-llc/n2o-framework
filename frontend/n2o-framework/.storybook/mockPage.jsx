import React from 'react';
import Single from '../src/components/layouts/Single/Single';
import Section from '../src/components/layouts/Section';
import Collapse, { Panel } from '../src/components/snippets/Collapse/Collapse';

export default () => <Single>
  <Section place="single">
    <Collapse activeKey="one">
      <Panel key="one" header="Первый">Лист 1</Panel>
      <Panel key="two" header="Второй">Лист 2</Panel>
      <Panel key="three" header="Третий">Лист 3</Panel>
      <Panel key="four" header="Четвертый">Лист 4</Panel>
    </Collapse>
  </Section>
</Single>