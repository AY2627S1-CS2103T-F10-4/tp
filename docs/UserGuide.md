---
layout: page
title: User Guide
---

AddressBook Level 3 (AB3) is a **desktop application for managing contacts, optimized for use
through a Command Line Interface (CLI)** while retaining the benefits of a Graphical User Interface
(GUI). If you type quickly, AB3 can help you manage contacts faster than traditional GUI
applications.

* Table of Contents
{:toc}

---------------------------------------------------------------------------------------------------

## Quick start

1.  Ensure that Java `25` or later is installed on your computer.
    
    **Mac users:** Ensure you have the precise JDK version prescribed
    [here](https://se-education.org/guides/tutorials/javaInstallationMac.html).

1.  Download the latest `.jar` file from [here](https://github.com/se-edu/addressbook-level3/releases).

1.  Copy the file to the folder you want to use as the _home folder_ for your AddressBook.

1.  Open your terminal and run the following commands,
    replacing `HOME` with the path to your application home folder:

    **Windows**

    ```bat
    cd /D HOME
    java -jar addressbook.jar
    ```

    **Linux and macOS**

    ```bash
    cd HOME
    java -jar addressbook.jar
    ```

    A GUI similar to the one below should appear in a few seconds.
    The app contains some sample data for you to get used to the UI.
    ![Ui sample](images/Ui.png)

1.  Type a command in the command box and press Enter to execute it.
    For example, type **`help`** and press Enter to open the help window.

    Some example commands you can try:

    * `listj`: Lists all unhidden jobseekers.
    * `listc`: Lists all unhidden clients.
    * `clearj`: Deletes all jobseekers.
    * `clearc`: Deletes all clients.
    * `addj n/John Doe p/98765432 e/johnd@example.com a/John street, block 123, #01-01`:
      Adds a jobseeker named `John Doe` to the Address Book.
    * `addc n/Jane Tan p/91234567 e/jane@example.com a/123 Clementi Road r/Hiring engineers t/employer`:
      Adds a client named `Jane Tan` with a remark and tag.
    * `deletej 3`: Deletes the third jobseeker shown in the current list.
    * `exit`: Exits the app.

1.  Refer to the [Features](#features) section below for details of each command.

---------------------------------------------------------------------------------------------------

## Features

<div markdown="block" class="alert alert-info">

**:information_source: Notes about the command format:**

* Words inside angle bracket `<field name>` are the parameters to be supplied by the user,
  hereby called _fields_.

  Field values can contain spaces, but preceding and trailing spaces are ignored.

  For example, in `addj n/<name>`, replace `<name>` with a value such as `John Doe`.
* Fields in square brackets are optional. Every other items are required.

  For example, `n/<name> [t/<tag>]` can be written as `n/John Doe t/friend` or as `n/John Doe`.
* Items followed by `...` can appear more than once. Every other items must only appear once.

  For example, `[t/<tag>]...` may be omitted, or written as `t/friend` or `t/friend t/family`.
* Prefixed parameters can be in any order.

  For example, if the command specifies `n/<name> p/<phone number>`, `p/<phone number> n/<name>`
  is also acceptable.

  For `editj`, `editc`, `tagj`, `tagc`, `untagj`, and `untagc`, 
  the index must come first, followed by the fields or tags to update.
* Extraneous parameters for commands that take no parameters,
  such as `help`, `exit`, `listj`, `listc`, `clearj`, and `clearc`, are ignored.

  For example, `help 123` is interpreted as `help`.
* If you are using a PDF version of this document, be careful when copying and pasting commands
  that span multiple lines as space characters surrounding line-breaks may be omitted when
  copied over to the application.

</div>

### Basic commands

#### Viewing help: `help`

Shows a copy of this help page inside the application.

**Format:**{: #help-format} `help`

#### Exiting the program: `exit`

Exits the program.

**Format:**{: #exit-format} `exit`

### Adding and modifying contacts

#### Common info about contacts

Jobseeker contacts contains the following values, and their corresponding constraints:

* Name
  * can contain any characters except `/` (e.g. `Beyoncé` and `Zoë` are allowed)
  * must not be blank
* Phone number
  * contains only digits
  * must be at least 3 digits long
* Email address
  * must follow the conventional format of email address
* Physical address
  * can be any value other than empty
* Remarks about the individual
  * can be any value
* Tags
  * can have multiple unique tags
  * See [Managing tags](#managing-tags) for more details about tags.

Client contacts contains the following values, and their corresponding constraints:

* Name
  * can contain any characters except `/` (e.g. `Ben & Jerry's`, `Pokémon` and `7-Eleven` are allowed)
  * must not be blank
* Phone number
  * contains only digits
  * must be at least 3 digits long
* Email address
  * must follow the conventional format of email address
* Physical address
  * can be any value other than empty
* Remarks about the individual
  * can be any value
* Tags
  * can have multiple unique tags
  * See [Managing tags](#managing-tags) for more details about tags.

Contacts are identified by their phone number **and** their email address.

* Contacts with the same phone number and email address are treated as the same contacts,
  even if their other fields differs.

* If contacts only have **either** the same phone number or email address,
  they are treated as different contacts.

#### Adding a jobseeker: `addj`

Adds a jobseeker to the address book.

Adding duplicate jobseeker contacts is not allowed.
However, adding an existing _client_ contact to the jobseeker list is allowed

**Format:**{: #addj-format} `addj n/<name> p/<phone number> e/<email> a/<address> [r/<remarks>] [t/<tag>]...`

* `<name>`: the name of the jobseeker
* `<phone number>`: the phone number of the jobseeker
* `<email>`: the email address of the jobseeker
* `<address>`: the address of the jobseeker
* `<remarks>`: your remarks about this jobseeker
  * Optional. When omitted, the remarks value is treated as empty.
* `<tag>`: a tag for this jobseeker
  * Optional. When omitted, no tags are added.

See [Common info about contacts](#common-info-about-contacts) for
additional constraints on field values.

**Examples:**
{: #addj-examples}

* `addj n/John Doe p/98765432 e/johnd@example.com a/John street, block 123, #01-01`
* `addj n/Betsy Crowe t/friend e/betsycrowe@example.com a/Newgate Prison p/1234567 t/criminal`

#### Adding a client: `addc`

Adds a client to the address book.

Adding duplicate client contacts is not allowed.
However, adding an existing _jobseeker_ contact to the client list is allowed

**Format:**{: #addc-format} `addc n/<name> p/<phone number> e/<email> a/<address> [r/<remarks>] [t/<tag>]...`

The fields are the same as `addj`. See [Adding a jobseeker: `addj` - Format](#addj-format).

**Examples:**
{: #addc-examples}

* `addc n/Jane Tan p/91234567 e/jane@example.com a/123 Clementi Road`
* `addc n/Jane Tan p/91234567 e/jane@example.com a/123 Clementi Road r/Hiring software engineers t/employer t/technology`

#### Editing a jobseeker: `editj`

Edits an existing jobseeker in the currently displayed jobseeker list.

The command overwrites some of the jobseeker's contact details, while leaving the rest unmodified.

Similar to [`addj`](#adding-a-jobseeker-addj), edits that would create duplicate jobseekers are rejected.

**Format:**{: #editj-format} `editj <index> [n/<name>] [p/<phone number>] [e/<email>] [a/<address>] [r/<remarks>] [t/<tag>]...`

At least one of the fields `<name>`, `<phone number>`, `<email>`, `<address>`, `<remarks>`,
or `<tag>` must be provided.

* `<index>`: the index of the jobseeker to edit
  * must be a positive integer, from 1 to the size of the currently displayed jobseeker list,
    inclusive
* `<name>`: the name of the jobseeker
* `<phone number>`: the phone number of the jobseeker
* `<email>`: the email address of the jobseeker
* `<address>`: the address of the jobseeker
* `<remarks>`: your remarks about this jobseeker
* `<tag>`: a tag for this jobseeker

**Notes:**
{: #editj-notes}

* To clear remarks, specify `r/` without any values.
* Supplied tags replace all existing tags. To clear all tags, specify `t/` without any values.
* Editing preserves hidden status and returns the view to the normal jobseeker list.
  An edited hidden jobseeker remains hidden.

**Examples:**
{: #editj-examples}

* Update the first displayed jobseeker's phone and email:
  `editj 1 p/91234567 e/johndoe@example.com`
* Replace the second displayed jobseeker's remark:
  `editj 2 r/Available immediately`
* Clear the second displayed jobseeker's remark and tags:
  `editj 2 r/ t/`

#### Editing a client: `editc`

Edits an existing client in the currently displayed client list.

The command overwrites some of the client's contact details, while leaving the rest unmodified.

Similar to [`addc`](#adding-a-client-addc), edits that would create duplicate clients are rejected.

**Format:**{: #editc-format} `editc <index> [n/<name>] [p/<phone number>] [e/<email>] [a/<address>] [r/<remarks>] [t/<tag>]...`

The fields are the same as `editj`. See [Editing a jobseeker: `editj` - Format](#editj-format)

**Notes:**
{: #editc-notes}

The notes are the same as `editj`. See [Editing a jobseeker: `editj` - Notes](#editj-notes)

**Examples:**
{: #editc-examples}

* Update the first displayed client's remark:
  `editc 1 r/Hiring software engineers`
* Clear that client's remark:
  `editc 1 r/`

#### Deleting a jobseeker: `deletej`

Deletes a jobseeker from the currently displayed jobseeker list.

**Format:**{: #deletej-format} `deletej <index>`

* `<index>`: the index of the jobseeker to delete
  * must be a positive integer, from 1 to the size of the currently displayed jobseeker list,
    inclusive

**Examples:**
{: #deletej-examples}

* Delete the second displayed jobseeker: `deletej 2`

#### Deleting a client: `deletec`

Deletes a client from the currently displayed client list.

**Format:**{: #deletec-format} `deletec <index>`

* `<index>`: the index of the client to delete
  * must be a positive integer, from 1 to the size of the currently displayed client list,
    inclusive

**Examples:**
{: #deletec-examples}

* Delete the third displayed client: `deletec 3`

#### Clearing all jobseekers: `clearj`

Clears all contacts from the jobseeker list.

This includes contacts that aren't listed in the currently displayed jobseeker list

**Format:**{: #clearj-format} `clearj`

#### Clearing all clients: `clearc`

Clears all contacts from the client list.

This includes contacts that aren't listed in the currently displayed client list

**Format:**{: #clearc-format} `clearc`

### Viewing contacts

There are 2 contact lists that operate independently:
the jobseeker list (on the right) and the client list (on the left)

#### Listing jobseekers: `listj`

Shows all visible (not hidden) jobseekers in the jobseeker list,
and clears the current jobseeker filter.

For more information on hidden jobseekers, see [Hiding a jobseeker: `hidej`](#hiding-a-jobseeker-hidej).

To list hidden jobseekers, see [Listing hidden jobseekers: `listhj`](#listing-hidden-jobseekers-listhj).

**Format:**{: #listj-format} `listj`

#### Listing clients: `listc`

Shows all visible (not hidden) clients in the client list,
and clears the current client filter.

For more information on hidden clients, see [Hiding a client: `hidec`](#hiding-a-client-hidec).

To list hidden clients, see [Listing hidden clients: `listhc`](#listing-hidden-clients-listhc).

**Format:**{: #listc-format} `listc`

#### Finding jobseekers: `findj`

Finds jobseekers whose name, phone number, email or tags contain any of the given keywords.

The search is case-insensitive and is inclusive, as in:

* inputting `hans` or `Hans` both matches `Hans Gruber`
* inputting `Hans Bo` matches both `Hans Gruber` and `Bo Yang`.

For more information on hidden jobseekers, see [Hiding a jobseekers: `hidej`](#hiding-a-jobseeker-hidej).

To search among hidden jobseekers, see [Finding hidden jobseekers: `findhj`](#finding-hidden-jobseekers-findhj).

**Format:**{: #findj-format} `findj <keyword>...`

* `<keyword>`: a keyword to search for
  * At least one keyword must be given.

**Notes:**
{: #findj-notes}

* Partial words match.

  For example, `Han` matches `Hans`, and `9435` matches the phone number `94351253`.
* Keyword order does not matter.

  For example, `Hans Bo` is equivalent to `Bo Hans`.
* Only the name, phone number, email and tags are searched. Addresses and remarks are not.
* Each keyword must match within a single field.

  For example, `Yeoh8743` does not match a jobseeker named `Alex Yeoh`
  whose phone number is `87438807`.

**Examples:**
{: #findj-examples}

* `findj alex david` returns `Alex Yeoh` and `David Li`.
* `findj li` returns `Charlotte Oliveiro` and `David Li`, since `li` appears inside both names.
* `findj 9103` returns `David Li`, whose phone number is `91031282`.
* `findj friends` returns `Alex Yeoh` and `Bernice Yu`, who are both tagged `friends`.

#### Finding clients: `findc`

Finds clients whose name, phone number, email or tags contain any of the given keywords.

The search is case-insensitive and is inclusive.

For more information on hidden clients, see [Hiding a client: `hidec`](#hiding-a-client-hidec).

To search among hidden clients, see [Finding hidden clients: `findhc`](#finding-hidden-clients-findhc).

**Format:**{: #findc-format} `findc <keyword>...`

* `<keyword>`: a keyword to search for
  * At least one keyword must be given.

**Notes:**
{: #findc-notes}

The notes are the same as `findj`. See [Finding jobseekers: `findj` - Notes](#findj-notes).

**Examples:**
{: #findc-examples}

* `findc roy` returns `Roy Balakrishnan`.
* `findc irfan@example.com` returns `Irfan Ibrahim`.
* `findc colleagues` returns `Roy Balakrishnan`, who is tagged `colleagues`.

#### Hiding a jobseeker: `hidej`

Hides the specified jobseeker, so that [`listj`](#listing-jobseekers-listj)
and [`findj`](#finding-jobseekers-findj) no longer show them.

Unlike deleting, hiding keeps the jobseeker and all their details in the address book.

To list hidden jobseekers,
see [Listing hidden jobseekers: `listhj`](#listing-hidden-jobseekers-listhj).

To search among hidden jobseekers,
see [Finding hidden jobseekers: `findhj`](#finding-hidden-jobseekers-findhj).

**Format:**{: #hidej-format} `hidej <index>`

* `<index>`: the index of the jobseeker to hide
  * must be a positive integer, from 1 to the size of the currently displayed jobseeker list,
    inclusive

**Examples:**
{: #hidej-examples}

* `listj` followed by `hidej 2` hides the 2nd jobseeker in the jobseeker list.
* `findj Betsy` followed by `hidej 1` hides the 1st jobseeker in the results of the `findj` command.

#### Hiding a client: `hidec`

Hides the specified client, so that [`listc`](#listing-clients-listc)
and [`findc`](#finding-clients-findc) no longer show them.

Unlike deleting, hiding keeps the client and all their details in the address book.

To list hidden clients,
see [Listing hidden clients: `listhc`](#listing-hidden-clients-listhc).

To search among hidden clients,
see [Finding hidden clients: `findhc`](#finding-hidden-clients-findhc).

**Format:**{: #hidec-format} `hidec <index>`

* `<index>`: the index of the client to hide
  * must be a positive integer, from 1 to the size of the currently displayed client list,
    inclusive

**Examples:**
{: #hidec-examples}

* `listc` followed by `hidec 1` hides the 1st client in the client list.

#### Unhiding a jobseeker: `unhidej`

Make a hidden jobseeker visible to [`listj`](#listing-jobseekers-listj)
and [`findj`](#finding-jobseekers-findj) again.

This command must be used while [`listhj`](#listing-hidden-jobseekers-listhj)
or [`findhj`](#finding-hidden-jobseekers-findhj) is in effect.

For more information on hidden jobseekers, see [Hiding a jobseeker: `hidej`](#hiding-a-jobseeker-hidej).

**Format:**{: #unhidej-format} `unhidej <index>`

* `<index>`: the index of the jobseeker to unhide
  * must be a positive integer, from 1 to the size of the currently displayed jobseeker list,
    inclusive

**Examples:**
{: #unhidej-examples}

* `listhj` followed by `unhidej 1` restores the first displayed hidden jobseeker.

#### Unhiding a client: `unhidec`

Make a hidden client visible to [`listc`](#listing-clients-listc)
and [`findc`](#finding-clients-findc) again.

This command must be used while [`listhc`](#listing-hidden-clients-listhc)
or [`findhc`](#finding-hidden-clients-findhc) is in effect.

For more information on hidden clients, see [Hiding a client: `hidec`](#hiding-a-client-hidec).

**Format:**{: #unhidec-format} `unhidec <index>`

* `<index>`: the index of the client to unhide
  * must be a positive integer, from 1 to the size of the currently displayed client list,
      inclusive

**Examples:**
{: #unhidec-examples}

* `listhc` followed by `unhidec 1` restores the first displayed hidden client.

#### Listing hidden jobseekers: `listhj`

Shows all _hidden_ jobseekers in the jobseeker list,
and clears the current jobseeker filter.

For more information on hidden jobseekers, see [Hiding a jobseeker: `hidej`](#hiding-a-jobseeker-hidej).

**Format:**{: #listhj-format} `listhj`

#### Listing hidden clients: `listhc`

Shows all _hidden_ clients in the client list,
and clears the current clients filter.

For more information on hidden clients, see [Hiding a client: `hidec`](#hiding-a-client-hidec).

**Format:**{: #listhc-format} `listhc`

#### Finding hidden jobseekers: `findhj`

Finds _hidden_ jobseekers whose name, phone number, email or tags contain any of the given keywords.

The search logic is the same as `findj`.
See [Finding jobseekers: `findj`](#finding-jobseekers-findj) for more details.

**Format:**{: #findhj-format} `findhj <keyword>...`

* `<keyword>`: a keyword to search for
  * At least one keyword must be given.

**Examples:**
{: #findhj-examples}

* `findhj alex` returns `Alex Yeoh`, if `Alex Yeoh` has been hidden.

#### Finding hidden clients: `findhc`

Finds _hidden_ clients whose name, phone number, email or tags contain any of the given keywords.

The search logic is the same as `findc`.
See [Finding clients: `findc`](#finding-clients-findc) for more details.

**Format:**{: #findhc-format} `findhc <keyword>...`

* `<keyword>`: a keyword to search for
  * At least one keyword must be given.

**Examples:**
{: #findhc-examples}

* `findhc irfan` returns `Irfan Ibrahim`, if `Irfan Ibrahim` has been hidden.

### Managing Tags

Tags are labels with arbitrary names that you can put on jobseekers and clients.
They can be used to help you organize contacts and speed up searches.

Tags contains the following attributes, and their corresponding constraints:

* Name
  * must contain only alphanumeric characters
  * is _case-sensitive_, unlike the parameters to `findj` and `findc`

Tags are identified by their names, i.e. 2 tags with the same name,
_case-sensitive_, are considered the same tags.

A jobseeker or client contact can have multiple tags, or no tags at all.

#### Adding jobseeker tags: `tagj`

Adds tags to a jobseeker while preserving existing tags.

**Format:**{: #tagj-format} `tagj <index> <tag>...`

* `<index>`: the index of the jobseeker to add the tag to
  * must be a positive integer, from 1 to the size of the currently displayed jobseeker list,
    inclusive
* `<tag>`: the name of a tag to add
  * At least one tag name must be given.

**Examples:**
{: #tagj-examples}

* `tagj 1 java` adds `java` to the 1st displayed jobseeker.
* `tagj 2 python teamwork` adds `python` and `teamwork` to the 2nd displayed jobseeker.

#### Adding client tags: `tagc`

Adds tags to a client while preserving existing tags.

**Format:**{: #tagc-format} `tagc <index> <tag>...`

* `<index>`: the index of the client to add the tag to
  * must be a positive integer, from 1 to the size of the currently displayed client list,
    inclusive
* `<tag>`: the name of a tag to add
  * At least one tag name must be given.

**Examples:**
{: #tagc-examples}

* `tagc 1 priority` adds `priority` to the 1st displayed client.
* `tagc 2 finance technology` adds `finance` and `technology` to the 2nd displayed client.

#### Removing jobseeker tags: `untagj`

Removes specified tags from a jobseeker while preserving other tags.

**Format:**{: #untagj-format} `untagj <index> <tag>...`

* `<index>`: the index of the jobseeker to remove the tag from
  * must be a positive integer, from 1 to the size of the currently displayed jobseeker list,
    inclusive
  * `<tag>`: the name of a tag to add
    * At least one tag name must be given.

**Examples:**
{: #untagj-examples}

* `untagj 1 java` removes `java` from the 1st displayed jobseeker.
* `untagj 2 python teamwork` removes `python` and `teamwork` from the 2nd displayed jobseeker.


#### Removing client tags: `untagc`

Removes specified tags from a client while preserving other tags.

**Format:**{: #untagc-format} `untagc <index> <tag>...`

* `<index>`: the index of the client to remove the tag from 
  * must be a positive integer, from 1 to the size of the currently displayed client list,
    inclusive
* `<tag>`: the name of a tag to add
  * At least one tag name must be given.

**Examples:**
{: #untagc-examples}

* `untagc 1 priority` removes `priority` from the 1st displayed client.
* `untagc 2 finance technology` removes `finance` and `technology` from the 2nd displayed client.

### Miscellaneous commands

#### Copying displayed jobseekers: `cpj`

Copies the details of all currently displayed jobseekers to the system clipboard.

**Format:**{: #cpj-format} `cpj`

**Notes:**
{: #cpj-notes}

* The copied details follow the order shown in the jobseeker list.
* Each jobseeker is copied on a separate line.
* The command only copies whichever jobseekers are currently displayed.
  If no jobseekers are displayed, the clipboard is cleared.

**Examples:**
{: #cpj-examples}

* `findj java` followed by `cpj` copies the displayed search results.

#### Copying displayed clients: `cpc`

Copies the details of all currently displayed clients to the system clipboard.

**Format:**{: #cpc-format} `cpc`

**Notes:**
{: #cpc-notes}

The notes are the same as `cpj`.
See [Copying displayed jobseekers: `cpj` - Notes](#cpj-notes) for notes.

**Examples:**
{: #cpc-examples}

* `findc technology` followed by `cpc` copies the displayed search results.

### Saving the data

AddressBook automatically saves data after every command. You do not need to save manually.

### Revisit past commands

Use the up and down arrow keys to revisit commands from your command history.

### Autocomplete commands

Type part of a command (e.g. `list`) to see suggestions. Use `Up`/`Down` to select one,
then `Tab` or `Enter` to complete it.
Press `Enter` again to run the command, or `Escape` to dismiss suggestions.

### Editing the data file

AddressBook data is saved automatically as a JSON file `[JAR file location]/data/addressbook.json`.
Advanced users are welcome to update data directly by editing that data file.

<div markdown="span" class="alert alert-warning">

**:exclamation: Caution:**

If your changes make the data file invalid, AddressBook starts with an
empty address book at the next run.
The invalid file remains on disk until you run a command (AddressBook saves after every command).
Still, we recommend backing up the file before editing it.

Furthermore, certain edits can cause the AddressBook to behave in unexpected ways
(e.g., if a value entered is outside the acceptable range).
Therefore, edit the data file only if you are confident that you can update it correctly.

</div>

### Archiving data files `[coming in v2.0]`

_Details coming soon ..._

---------------------------------------------------------------------------------------------------

## FAQ

**Q:** How do I transfer my data to another computer?<br>
**A:** Install the app on the other computer and overwrite the data file it creates with the data file from your previous AddressBook home folder.

---------------------------------------------------------------------------------------------------

## Known issues

1. **When using multiple screens**, if you move the application to a secondary screen, and later switch to using only the primary screen, the GUI will open off-screen. The remedy is to delete the `preferences.json` file created by the application before running the application again.
2. **If you minimize the Help Window** and then run the `help` command (or use the `Help` menu, or the keyboard shortcut `F1`) again, the original Help Window will remain minimized, and no new Help Window will appear. The remedy is to manually restore the minimized Help Window.

---------------------------------------------------------------------------------------------------

## Command summary

| Action                                                         | Format                                                                                                  |
|----------------------------------------------------------------|---------------------------------------------------------------------------------------------------------|
| [Help](#viewing-help-help)                                     | `help`                                                                                                  |
| [Exit](#exiting-the-program-exit)                              | `exit`                                                                                                  |
| [Add jobseeker](#adding-a-jobseeker-addj)                      | `addj n/<name> p/<phone number> e/<email> a/<address> [r/<remarks>] [t/<tag>]...`                       |
| [Add client](#adding-a-client-addc)                            | `addc n/<name> p/<phone number> e/<email> a/<address> [r/<remarks>] [t/<tag>]...`                       |
| [Edit jobseeker](#editing-a-jobseeker-editj)                   | `editj <index> [n/<name>] [p/<phone number>] [e/<email>] [a/<address>] [r/<remarks>] [t/<tag>]...`      |
| [Edit client](#editing-a-client-editc)                         | `editc <index> [n/<name>] [p/<phone number>] [e/<email>] [a/<address>] [r/<remarks>] [t/<tag>]...`      |
| [Clear jobseekers](#clearing-all-jobseekers-clearj)            | `clearj`                                                                                                |
| [Clear clients](#clearing-all-clients-clearc)                  | `clearc`                                                                                                |
| [Delete jobseekers](#deleting-a-jobseeker-deletej)             | `deletej <index>`                                                                                       |
| [Delete clients](#deleting-a-client-deletec)                   | `deletec <index>`                                                                                       |
| [List jobseekers](#listing-jobseekers-listj)                   | `listj`                                                                                                 |
| [List clients](#listing-clients-listc)                         | `listc`                                                                                                 |
| [Find jobseekers](#finding-jobseekers-findj)                   | `findj <keyword>...`                                                                                    |
| [Find clients](#finding-clients-findc)                         | `findc <keyword>...`                                                                                    |
| [Find hidden jobseekers](#finding-hidden-jobseekers-findhj)    | `findhj <keyword>...`                                                                                   |
| [Find hidden clients](#finding-hidden-clients-findhc)          | `findhc <keyword>...`                                                                                   |
| [Hide jobseeker](#hiding-a-jobseeker-hidej)                    | `hidej <index>`                                                                                         |
| [Hide client](#hiding-a-client-hidec)                          | `hidec <index>`                                                                                         |
| [Unhide jobseeker](#unhiding-a-jobseeker-unhidej)              | `unhidej <index>`                                                                                       |
| [Unhide client](#unhiding-a-client-unhidec)                    | `unhidec <index>`                                                                                       |
| [List hidden jobseekers](#listing-hidden-jobseekers-listhj)    | `listhj`                                                                                                |
| [List hidden clients](#listing-hidden-clients-listhc)          | `listhc`                                                                                                |
| [Add jobseeker tags](#adding-jobseeker-tags-tagj)              | `tagj <index> <tag>...`                                                                                 |
| [Add client tags](#adding-client-tags-tagc)                    | `tagc <index> <tag>...`                                                                                 |
| [Remove jobseeker tags](#removing-jobseeker-tags-untagj)       | `untagj <index> <tag>...`                                                                               |
| [Remove client tags](#removing-client-tags-untagc)             | `untagc <index> <tag>...`                                                                               |
| [Copy displayed jobseekers](#copying-displayed-jobseekers-cpj) | `cpj`                                                                                                   |
| [Copy displayed clients](#copying-displayed-clients-cpc)       | `cpc`                                                                                                   |
