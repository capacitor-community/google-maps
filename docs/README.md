<p align="center"><br><img src="https://user-images.githubusercontent.com/236501/85893648-1c92e880-b7a8-11ea-926d-95355b8175c7.png" width="128" height="128" /></p>

<h3 id="home" align="center">Google Maps</h3>
<p align="center"><strong><code>@capacitor-community/google-maps</code></strong></p>
<p align="center">Capacitor Plugin using native Google Maps SDK for Android and iOS.</p>
<p align="center">
</p>

<p align="center">
  <img src="https://img.shields.io/badge/Capacitor%20V3%20Support-yes-green?logo=Capacitor&style=flat-square" />
  <img src="https://img.shields.io/maintenance/yes/2022?style=flat-square" />
  <a href="https://img.shields.io/github/workflow/status/capacitor-community/google-maps/Publish"><img src="https://img.shields.io/github/workflow/status/capacitor-community/google-maps/Build?style=flat-square" /></a>
  <a href="https://www.npmjs.com/package/@capacitor-community/google-maps"><img src="https://img.shields.io/npm/l/@capacitor-community/google-maps?style=flat-square" /></a>
<br>
  <a href="https://www.npmjs.com/package/@capacitor-community/google-maps"><img src="https://img.shields.io/npm/dw/@capacitor-community/google-maps?style=flat-square" /></a>
  <a href="https://www.npmjs.com/package/@capacitor-community/google-maps"><img src="https://img.shields.io/npm/v/@capacitor-community/google-maps?style=flat-square" /></a>
<!-- ALL-CONTRIBUTORS-BADGE:START - Do not remove or modify this section -->
<a href="#contributors"><img src="https://img.shields.io/badge/all%20contributors-13-orange?style=flat-square" /></a>
<!-- ALL-CONTRIBUTORS-BADGE:END -->
</p>

## Purpose

Under the hood this package makes use of the native Maps SDK for Android and iOS. The native Maps SDK has much better performance than the JS equivalent. It also adds support for offline caching. On top of that it ("Dynamic Maps") is completely free to use (as of February 2022) ([native pricing](https://developers.google.com/maps/billing-and-pricing/pricing)), in contrary to the JS SDK ([JS pricing](https://developers.google.com/maps/documentation/javascript/usage-and-billing#new-payg)).

See if this plugin is a good fit for your use case in [this section](about/should-you-use-this-plugin.md).

## Maintainers

| Maintainer   | GitHub                                  | Mail                                                       |
| ------------ | --------------------------------------- | ---------------------------------------------------------- |
| Hemang Kumar | [hemangsk](https://github.com/hemangsk) | <a href="mailto:hemangsk@gmail.com">hemangsk@gmail.com</a> |

## Limitations

- Right now, its not possible to allow the rendered Map view to scroll along with the page, it remains at its fixed position.
- This plugins only adds support for using the native iOS and Android SDK. If you want to make use of the JavaScript SDK as well (for a possible webapp) you should implement that separately. Of course it should possible to integrate this into the plugin in the future. If you want to help out with this, please start a PR.

## Contributors ✨

Thanks goes to these wonderful people ([emoji key](https://allcontributors.org/docs/en/emoji-key)):

<!-- ALL-CONTRIBUTORS-LIST:START - Do not remove or modify this section -->
<!-- prettier-ignore-start -->
<!-- markdownlint-disable -->
<table>
  <tr>
    <td align="center"><a href="https://maximus.agency/"><img src="https://avatars.githubusercontent.com/u/14840021?v=4?s=100" width="100px;" alt=""/><br /><sub><b>Grant Brits</b></sub></a><br /><a href="https://github.com/capacitor-community/google-maps/commits?author=gbrits" title="Code">💻</a> <a href="https://github.com/capacitor-community/google-maps/issues?q=author%3Agbrits" title="Bug reports">🐛</a> <a href="#ideas-gbrits" title="Ideas, Planning, & Feedback">🤔</a></td>
    <td align="center"><a href="https://github.com/tafelnl"><img src="https://avatars.githubusercontent.com/u/35837839?v=4?s=100" width="100px;" alt=""/><br /><sub><b>Tafel</b></sub></a><br /><a href="https://github.com/capacitor-community/google-maps/commits?author=tafelnl" title="Code">💻</a> <a href="https://github.com/capacitor-community/google-maps/issues?q=author%3Atafelnl" title="Bug reports">🐛</a> <a href="#ideas-tafelnl" title="Ideas, Planning, & Feedback">🤔</a></td>
    <td align="center"><a href="https://github.com/abcoskn"><img src="https://avatars.githubusercontent.com/u/6419471?v=4?s=100" width="100px;" alt=""/><br /><sub><b>abcoskn</b></sub></a><br /><a href="https://github.com/capacitor-community/google-maps/commits?author=abcoskn" title="Code">💻</a> <a href="https://github.com/capacitor-community/google-maps/issues?q=author%3Aabcoskn" title="Bug reports">🐛</a></td>
    <td align="center"><a href="https://github.com/hyun-yang"><img src="https://avatars.githubusercontent.com/u/2142419?v=4?s=100" width="100px;" alt=""/><br /><sub><b>Hyun Yang</b></sub></a><br /><a href="https://github.com/capacitor-community/google-maps/issues?q=author%3Ahyun-yang" title="Bug reports">🐛</a> <a href="#example-hyun-yang" title="Examples">💡</a></td>
    <td align="center"><a href="https://github.com/MelanieMarval"><img src="https://avatars.githubusercontent.com/u/43726363?v=4?s=100" width="100px;" alt=""/><br /><sub><b>Melanie Marval</b></sub></a><br /><a href="https://github.com/capacitor-community/google-maps/issues?q=author%3AMelanieMarval" title="Bug reports">🐛</a></td>
    <td align="center"><a href="https://github.com/tototares"><img src="https://avatars.githubusercontent.com/u/1064024?v=4?s=100" width="100px;" alt=""/><br /><sub><b>l4ke</b></sub></a><br /><a href="https://github.com/capacitor-community/google-maps/issues?q=author%3Atototares" title="Bug reports">🐛</a></td>
    <td align="center"><a href="https://github.com/quaz579"><img src="https://avatars.githubusercontent.com/u/13681950?v=4?s=100" width="100px;" alt=""/><br /><sub><b>Ben Grossman</b></sub></a><br /><a href="https://github.com/capacitor-community/google-maps/issues?q=author%3Aquaz579" title="Bug reports">🐛</a></td>
  </tr>
  <tr>
    <td align="center"><a href="https://github.com/gerciljunio"><img src="https://avatars.githubusercontent.com/u/4561073?v=4?s=100" width="100px;" alt=""/><br /><sub><b>Gercil Junio</b></sub></a><br /><a href="#userTesting-gerciljunio" title="User Testing">📓</a></td>
    <td align="center"><a href="https://github.com/aacassandra"><img src="https://avatars.githubusercontent.com/u/29236058?v=4?s=100" width="100px;" alt=""/><br /><sub><b>Alauddin Afif Cassandra</b></sub></a><br /><a href="#userTesting-aacassandra" title="User Testing">📓</a></td>
    <td align="center"><a href="https://github.com/togro"><img src="https://avatars.githubusercontent.com/u/7252575?v=4?s=100" width="100px;" alt=""/><br /><sub><b>togro</b></sub></a><br /><a href="https://github.com/capacitor-community/google-maps/issues?q=author%3Atogro" title="Bug reports">🐛</a> <a href="#userTesting-togro" title="User Testing">📓</a></td>
    <td align="center"><a href="https://www.selectedpixel.com/"><img src="https://avatars.githubusercontent.com/u/28204537?v=4?s=100" width="100px;" alt=""/><br /><sub><b>selected-pixel-jameson</b></sub></a><br /><a href="https://github.com/capacitor-community/google-maps/issues?q=author%3Aselected-pixel-jameson" title="Bug reports">🐛</a></td>
    <td align="center"><a href="https://github.com/ChiKaLiO"><img src="https://avatars.githubusercontent.com/u/12167528?v=4?s=100" width="100px;" alt=""/><br /><sub><b>chikalio</b></sub></a><br /><a href="https://github.com/capacitor-community/google-maps/issues?q=author%3AChiKaLiO" title="Bug reports">🐛</a></td>
    <td align="center"><a href="https://www.tickeri.com/"><img src="https://avatars.githubusercontent.com/u/1047598?v=4?s=100" width="100px;" alt=""/><br /><sub><b>Javier Gonzalez</b></sub></a><br /><a href="https://github.com/capacitor-community/google-maps/commits?author=J-Gonzalez" title="Code">💻</a></td>
  </tr>
</table>

<!-- markdownlint-restore -->
<!-- prettier-ignore-end -->

<!-- ALL-CONTRIBUTORS-LIST:END -->

This project follows the [all-contributors](https://github.com/all-contributors/all-contributors) specification. Contributions of any kind welcome!
