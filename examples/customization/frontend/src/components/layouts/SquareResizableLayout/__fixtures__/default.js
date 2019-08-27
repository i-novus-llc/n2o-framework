import React from "react";
import Section from "n2o-framework/lib/components/layouts/Section";
import SquareResizableLayout from "../SquareResizableLayout";

const TestLayout = () => {
  return (
    <SquareResizableLayout>
      <Section place="topLeft">
        Lorem ipsum dolor sit amet, consectetur adipiscing elit. Nulla et ligula
        eu dolor posuere aliquam. Sed eu varius eros. Duis vitae faucibus metus.
        Phasellus justo urna, faucibus maximus velit sit amet, hendrerit
        lobortis purus. Vivamus sit amet ipsum egestas erat bibendum euismod.
        Praesent id feugiat libero. Suspendisse non turpis condimentum, commodo
        velit at, consequat metus. Aenean imperdiet leo sit amet imperdiet
        efficitur. Donec laoreet, nunc eget cursus porttitor, felis ex elementum
        diam, non pellentesque ex tellus vitae sem. Cras in sem vel nulla tempus
        iaculis in ut lacus. Nam bibendum dui id accumsan rutrum. Nulla quam
        leo, facilisis at aliquam in, rutrum eget urna.
      </Section>
      <Section place="topRight">
        Sed malesuada neque sit amet ligula commodo sagittis. Pellentesque
        vulputate libero lorem, a feugiat est aliquam at. Mauris et leo
        hendrerit, imperdiet metus quis, vehicula velit. Nulla lacus risus,
        sodales vel viverra eu, lobortis vitae ante. Integer faucibus tellus
        quis tempor ultrices. Mauris in mauris sit amet tortor pharetra
        convallis. Mauris lobortis molestie justo in consequat. Sed dui felis,
        faucibus ac nunc ut, ullamcorper luctus dui. Etiam quis tempus elit.
        Donec mauris libero, blandit at venenatis id, tincidunt nec nisl. Nam a
        volutpat nisi. Pellentesque feugiat eros nec augue consectetur vehicula.
        Praesent placerat vitae ipsum vel dictum.
      </Section>
      <Section place="bottomLeft">
        Vestibulum eu arcu mattis, malesuada ipsum vitae, volutpat nisl. Ut
        vulputate sem eget metus dictum blandit. Aenean iaculis, ligula in
        sollicitudin commodo, quam nunc fermentum massa, sed maximus ipsum leo
        id mauris. In aliquam, enim eu consequat cursus, dolor ante porta sem, a
        venenatis leo orci sed purus. Etiam ex nunc, hendrerit et augue ac,
        sollicitudin iaculis lorem. Aenean non pulvinar arcu. Nam in turpis
        odio. Fusce malesuada, massa a tincidunt semper, odio quam feugiat
        mauris, ut pulvinar elit nisl vel nibh. Fusce vitae elementum quam, vel
        lobortis nunc. Vivamus mollis accumsan sapien vitae suscipit. Donec
        faucibus urna mattis odio pellentesque tempor.
      </Section>
      <Section place="bottomRight">
        Proin non imperdiet enim, vel dignissim ex. Nunc egestas felis sed velit
        convallis, in faucibus risus facilisis. Vestibulum ipsum purus, laoreet
        at velit sed, ullamcorper pretium quam. Sed rhoncus aliquam elit rutrum
        fringilla. Ut eu iaculis odio. Phasellus a molestie metus. Mauris
        pretium nunc sit amet accumsan dictum. Quisque elementum lacus in enim
        varius rhoncus. Proin interdum dolor ac urna egestas, non eleifend nibh
        hendrerit.
      </Section>
    </SquareResizableLayout>
  );
};

export default [
  {
    component: TestLayout,
    name: "Layout с изменяемыми размерами",
    props: {},
    reduxState: {},
    context: {}
  }
];
