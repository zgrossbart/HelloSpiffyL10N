[Spiffy UI Framework](http://www.spiffyui.org) - GWT made simple
==================================================

Hello Spiffy L10N is a sample application using the [Spiffy UI Framework](http://www.spiffyui.org) which builds a simple REST application with Apache Ant and shows you how to localize your Spiffy UI application.


Building and Running Spiffy UI
--------------------------------------

Hello Spiffy L10N is built with [Apache Ant](http://ant.apache.org/) using [Apache Ivy](http://ant.apache.org/ivy/).  Once you've installed Ant go to your HelloSpiffyL10N working copy and run this command:

        <ANT HOME>/ant
        
This will download Apache Ivy and the other the required libraries, build the Hello Spiffy L10N project, and run it with an embedded Jetty web server.  It will then provide instructions for accessing the running application once the build is completed.  

How It Works
--------------------------------------

This application was created with the [Spiffy UI project builder](http://www.spiffyui.org/#!getStarted) and then edited to support French.  You can see it work by setting your preferred locale to French in your browser.  To make this work I made the following changes in my project:

### Added localization support to my GWT module ###

To support localization in GWT you have to enabled in your GWT module file.  In this project that file is `src/main/java/org/spiffyui/hellospiffylocalization/index.gwt.xml`.

The first step is to import the localization module:

        <inherits name="com.google.gwt.i18n.I18N"/>
        
Then we need to specify which languages we want to support:

        <extend-property name="locale" values="fr" />
        
That will make sure the GWT compiler creates output for French as well as English.

### Created a locale filter ###

GWT can figure out the correct language based on the browser's installed locale, but not the preferred locale.  That means you would have to installed Mozilla in French instead of just specifying French in your preferences.  This is especially problematic because your browser can send one locale to the server and another one to GWT resulting an in application that half one language and half another.  It isn't GWT's fault, there just isn't a good way to get proper locale information in JavaScript.

The solution is a locale filter.  Spiffy UI comes with a set of servlet filters which inject the correct HTML meta tags for determining the locale for GWT.  The easiest one is [GWTLocaleBundleFilter](http://www.spiffyui.org/javadoc/org/spiffyui/server/filter/GWTLocaleBundleFilter.html).  This filter figures out the right locale by looking at the message bundles in your application.  We extend it with a one-line filter named `LocaleFilter.java`:

        public class LocaleFilter extends GWTLocaleBundleFilter
        {
            @Override
            protected String getResourcePath()
            {
                return "/WEB-INF/classes/org/spiffyui/hellospiffylocalization/client";
            }
        }
        
This filter tells the GWTLocaleBundlerFilter where our properties files are and it makes sure we send the correct localized strings to the browser.  Now we just need those strings.

### Added localized strings ###

This application has user visible strings in two places.  The first is `src/main/java/org/spiffyui/hellospiffylocalization/client/MainPanel.html`.  This HTML file has most of the strings in our UI.  We can localize it by creating a new file:  `src/main/java/org/spiffyui/hellospiffylocalization/client/MainPanel_fr.html`.

This new file contains the same content but in French.  It still has the same HTML placeholders for our controls, they are just surrounded by French.  For example:

        Enter your name: <span id="nameField"></span> <span id="submitButton"></span>
        
becomes

        Entrez votre nom: <span id="nameField"></span> <span id="submitButton"></span>
        
Once we've translated the entire file we make sure to save it in [UTF-8](http://en.wikipedia.org/wiki/UTF-8) format.

The rest of the strings for our application are located in `src/main/java/org/spiffyui/hellospiffylocalization/client/Index.java`.  These are just a few strings that are used with dynamic content.  For these strings we create a GWT [Messages](http://google-web-toolkit.googlecode.com/svn/javadoc/2.1/com/google/gwt/i18n/client/Messages.html) class.

Our Messages class is `src/main/java/org/spiffyui/hellospiffylocalization/client/ProjStrings.java`.  It is a simple interface with one method for each localized string like this:

        public interface ProjStrings extends Messages
        {
            public String hello();
            ...
            
Each string is in a properties file with a property of the same name as the method name.  For example in ProjStrings.properties:

        hello = Hello Spiffy Localization!
        ...
        
We then create a second file with the French strings like ProjStrings_fr.properties:

        hello = Salut Spiffy Localisation!
        ...
        
Once we've created this Message class we can call it from inside Index.java just by calling each method:

        ProjStrings PROJ_STRINGS = (ProjStrings) GWT.create(ProjStrings.class);
        header.setHeaderTitle(PROJ_STRINGS.hello());
        
### Putting it all together ###

That's all we need to support French in our application.  When you compile it you'll see 12 permutations from the GWT compiler, one for each combination of supported browser and locale.  When this page is run the filter will find the best matching locale and inject the header in the index.html file which will allow GWT to select the correct language for your page.

Your application can support as many languages you want.  You can send your properties files to a real localization company and you won't have to live with my horrible French.  

License
--------------------------------------

Hello Spiffy L10N is available under the [Apache License, Version 2.0](http://www.apache.org/licenses/LICENSE-2.0.html).


